package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.stores.MenuStore

class MenuScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.MENU_TITLE
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        // instancia única de menuStore en toda la app
        val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val menuEntityList = menuStore.menusList
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            Column(Modifier.weight(1f)) {
                MyScrollableColumn {
                    if (menuEntityList.value.isNotEmpty()) {
                        menuEntityList.value.forEach { menuEntity: MenuEntity ->
                            MenuRow(menuEntity, navigator, menuStore)
                            Spacer(Modifier.height(Tools.height16dp))
                        }
                    } else {
                        Text("Sin menús.")
                    }
                }
            }

            // *** Barra inferior de botones ***
            Row(Modifier.fillMaxWidth().weight(0.125f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = { menuStore.toggleShowAddMenuPopup(true) }) {
                    Text(Literals.ButtonsText.ADD_MENU)
                }
            }
        }

        if (menuStore.showAddMenuPopup.value) {
            AddMenuPopup(onDismiss = {
                menuStore.toggleShowAddMenuPopup(false)
                menuStore.deleteCheckedData()
            })
        }
    }

    @Composable
    fun MenuRow(menuEntity: MenuEntity, navigator: Navigator, menuStore: MenuStore) {
        Row(
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Black).padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column {
                Text(menuEntity.nombre, style = Tools.styleTitleUnderlineBlack)
            }

            Row {
                MyIcons.EditIcon { navigator.push(EditMenuScreen(menuEntity.id, menuEntity.nombre)) }
                Spacer(Modifier.width(Tools.buttonsSpacer8dp))
                MyIcons.TrashIcon { menuStore.deleteMenu(menuEntity) }
            }
        }
    }
}