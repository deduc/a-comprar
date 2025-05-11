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
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
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
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas
import org.ivandev.acomprar.stores.MenuStore

class EditMenuScreen(
    private val menuId: Int,
    private val menuName: String
): Screen {
    @Composable
    override fun Content() {
        val menu = MenuEntity(menuId, menuName)
        val screen = CommonScreen(title = menuName) {
            MainContent(menu)
        }

        screen.Render()
    }

    @Composable
    fun MainContent(menuEntity: MenuEntity) {
        // instancia única de menuStore en toda la app
        val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        menuStore.setEditingMenu(menuEntity)

        var editingMenu = menuStore.editingMenu
        val menuDaysOfWeek: SnapshotStateList<MenuDaysOfWeekEntity> = remember { mutableStateListOf<MenuDaysOfWeekEntity>() }
        var menuName = remember { mutableStateOf(editingMenu.value!!.nombre) }

        LaunchedEffect(menuEntity.id) {
            menuDaysOfWeek.clear()

            menuDaysOfWeek.addAll(
                menuStore.getMenuDaysOfWeekByMenuId(menuEntity.id)
            )
        }

        if (editingMenu.value != null) {
            MyScrollableColumn {
                MenuTitleFormulary(menuName, editingMenu.value!!, menuStore)
                Spacer(Modifier.height(Tools.height16dp))
                MenuComidasYCenasFormulary(menuStore, menuDaysOfWeek)
            }

            if (menuStore.addOrChangeComida.value) {
                AddOrEditComidaInMenuPopup(
                    menuEntity.id,
                    onDismiss = {
                        menuStore.setAddOrChangeComida(false)
                        menuStore.setAddOrChangeProductoPopup(false)
                        menuStore.setMenuDaysOfWeekClicked(null)
                    }
                )
            }
            else if (menuStore.addOrChangeCena.value) {
                AddOrEditComidaInMenuPopup(
                    menuEntity.id,
                    onDismiss = {
                        menuStore.setAddOrChangeCena(false)
                        menuStore.setAddOrChangeProductoPopup(false)
                        menuStore.setMenuDaysOfWeekClicked(null)
                    }
                )
            }
        }
        else {
            Text("Error inesperado que no permite visualizar el menú.")
        }
    }

    @Composable
    fun MenuTitleFormulary(menuName: MutableState<String>, menuEntity: MenuEntity, menuStore: MenuStore) {
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
    fun MenuComidasYCenasFormulary(menuStore: MenuStore, menuDaysOfWeek: SnapshotStateList<MenuDaysOfWeekEntity>) {
        val headers: List<String> = listOf(
            Literals.UITables.DIA_COLUMN,
            Literals.UITables.COMIDA_COLUMN,
            Literals.UITables.CENA_COLUMN
        )

        Column(Tools.styleBorderBlack) {
            TableHeaders(headers)

            // Mostrar las comidas y cenas por cada día de la semana
            val columnModifier = Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp)

            menuDaysOfWeek.forEach { menuDaysOfWeek: MenuDaysOfWeekEntity ->
                val comida: ComidaEntity? = menuStore.getComidaById(menuDaysOfWeek.idComida)
                val cena: ComidaEntity? = menuStore.getCenaById(menuDaysOfWeek.idCena)

                Row(Tools.styleBorderBlack, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Column(columnModifier) {
                        Text(menuDaysOfWeek.day)
                    }

                    Column(columnModifier) {
                        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            if (comida != null) {
                                Text(comida.nombre)
                            } else {
                                MyIcons.AddIcon{
                                    menuStore.setAddOrChangeProductoPopup(true)
                                    menuStore.setAddOrChangeComida(true)
                                    menuStore.setMenuDaysOfWeekClicked(menuDaysOfWeek)
                                }
                            }
                        }
                    }

                    Column(columnModifier) {
                        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            if (cena != null) {
                                Text(cena.nombre)
                            } else {
                                MyIcons.AddIcon() {
                                    menuStore.setAddOrChangeProductoPopup(true)
                                    menuStore.setAddOrChangeCena(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TableHeaders(headers: List<String>) {
        Row(Tools.styleBorderBlack) {
            headers.forEach { header ->
                Text(header, Modifier
                    .weight(1f)
                    .border(1.dp, Color.Black)
                    .padding(8.dp), style = Tools.styleTableHeader)
            }
        }
    }

    @Composable
    fun EditableComida(comidasYCenas: MyMenuComidas, index: Int) {
        if (comidasYCenas.comidaEntities.size <= index) {
            Row {
                MyIcons.AddIcon() {}
                Text("HOLA")
            }
        }
        else {
            Row {
                Text(
                    comidasYCenas.comidaEntities[index]?.nombre ?: "aaa",
                    Modifier.clickable {  }
                )
            }
        }
    }
}