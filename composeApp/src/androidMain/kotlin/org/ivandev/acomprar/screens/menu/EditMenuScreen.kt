package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.components.TextWhite
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.screens.comida.AddComidaPopup
import org.ivandev.acomprar.stores.ComidaStore
import org.ivandev.acomprar.stores.MenuStore

class EditMenuScreen(
    private val menuId: Int,
    private val menuName: String
): Screen {
    @Composable
    override fun Content() {
        val menu = MenuEntity(menuId, menuName)
        CommonScreen(title = menuName) { MainContent(menu) }.Render()
    }

    @Composable
    fun MainContent(menuEntity: MenuEntity) {
        // instancia única de menuStore en toda la app
        val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        menuStore.setEditingMenu(menuEntity)

        var editingMenu: State<MenuEntity?> = menuStore.editingMenu
        val menuDaysOfWeekList: SnapshotStateList<MenuDaysOfWeek> = menuStore.menuDaysOfWeekList
        var menuName: MutableState<String> = remember { mutableStateOf(editingMenu.value!!.nombre) }

        LaunchedEffect(menuEntity.id) {
            menuStore.getMenuDaysOfWeekByMenuId(menuEntity.id)
        }

        Column {
            MyScrollableColumn(Modifier.weight(1f)) {
                MenuTitleFormulary(menuName, editingMenu.value!!)
                Spacer(Modifier.height(Tools.height16dp))
                MenuComidasYCenasTable(menuDaysOfWeekList)
            }

            ButtonsPanel(comidaStore)

            Popups(menuStore, comidaStore, menuDaysOfWeekList)
        }
    }

    @Composable
    fun MenuTitleFormulary(menuName: MutableState<String>, menuEntity: MenuEntity) {
        val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = menuName.value,
                label = { Literals.ADD_MENU_TITLE },
                onValueChange = { menuName.value = it }
            )

            MyIcons.SaveIcon {
                val newMenu = MenuEntity(menuEntity.id, menuName.value)

                menuStore.updateMenuNameById(newMenu) { result ->
                    if (result) {
                        Tools.Notifier.showToast("Nuevo nombre guardado.")
                    } else {
                        Tools.Notifier.showToast("ERROR inesperado.")
                    }
                }
            }
        }
    }

    @Composable
    fun MenuComidasYCenasTable(menuDaysOfWeekList: SnapshotStateList<MenuDaysOfWeek>) {
        val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        val headers: List<String> = Literals.UITables.getComidasYCenasTableHeaders()
        val days: List<String> = menuDaysOfWeekList.map { it.day!! }.toSet().toList()

        Column(Tools.styleBorderBlack) {
            val rowModifier = Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp)
            var isComida = true

            TableHeaders(headers)

            days.forEach { currentDay: String ->
                val menuDOWCurrentDay = menuDaysOfWeekList.filter { it.day == currentDay }

                Row(Tools.styleBorderBlack, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(currentDay, rowModifier)

                    menuDOWCurrentDay.forEach { menuDaysOfWeek: MenuDaysOfWeek ->

                        Column(rowModifier) {
                            ComidaCell(menuStore, menuDaysOfWeek, isComida)
                        }
                        isComida = !isComida
                    }
                }
            }
        }
    }

    @Composable
    fun ComidaCell(menuStore: MenuStore, menuDaysOfWeek: MenuDaysOfWeek, isComida: Boolean) {
        val comida: ComidaEntity? = menuStore.getComidaByComidaId(menuDaysOfWeek.idComida)

        val addOrChangeComidaClickableFun: () -> Unit = {
            menuStore.setAddOrChangeProductoPopup(true)
            menuStore.isComidaClickedAux.value = isComida
            menuStore.setMenuDaysOfWeekClicked(menuDaysOfWeek)
        }

        Row(Modifier.fillMaxSize().clickable { addOrChangeComidaClickableFun() }, horizontalArrangement = Arrangement.Center) {
            if (comida != null && comida.id != 0) {
                Text(comida.nombre)
            }
            else {
                MyIcons.AddIcon { addOrChangeComidaClickableFun() }
            }
        }
    }

    @Composable
    fun TableHeaders(headers: List<String>) {
        Row(Tools.styleBorderBlack) {
            headers.forEach { header ->
                Text(header, Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp), style = Tools.styleTableHeader)
            }
        }
    }

    @Composable
    fun Popups(menuStore: MenuStore, comidaStore: ComidaStore, menuDaysOfWeekList: SnapshotStateList<MenuDaysOfWeek>){
        if (menuStore.addOrChangeProductoPopup.value) {
            AddOrEditComidaInMenuPopup(
                menuDaysOfWeekEntity = menuDaysOfWeekList.find { it.id == menuStore.menuDaysOfWeekClicked.value!!.id }!!,
                onDismiss = {
                    menuStore.setAddOrChangeProductoPopup(false)
                    menuStore.setMenuDaysOfWeekClicked(null)
                }
            )
        }

        if (comidaStore.showAddComidaPopup.value) {
            AddComidaPopup()
        }
    }

    @Composable
    fun ButtonsPanel(comidaStore: ComidaStore) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { comidaStore.setShowAddComidaPopup(true) }) {
                TextWhite(Literals.ButtonsText.ADD_COMIDA)
            }
        }
    }
}