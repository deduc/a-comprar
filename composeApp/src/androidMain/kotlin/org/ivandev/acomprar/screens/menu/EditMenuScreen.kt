package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.screens.menu.classes.MyComidasYCenas
import org.ivandev.acomprar.stores.MenuStore

class EditMenuScreen(
    private val menu: Menu
): Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(title = Literals.ADD_MENU_TITLE) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        val menuStore: MenuStore = viewModel()
        var menuName = remember { mutableStateOf(menu.nombre) }

        TextField(
            value = menuName.value,
            label = { Literals.ADD_MENU_TITLE },
            onValueChange = { menuName.value = it }
        )

        Spacer(Modifier.height(Tools.height16dp))

        MenuFormulary(menuStore)
    }

    @Composable
    fun MenuFormulary(menuStore: MenuStore) {
        val HEADERS: List<String> = listOf(
            Literals.Table.DIA_COLUMN,
            Literals.Table.COMIDA_COLUMN,
            Literals.Table.CENA_COLUMN
        )
        val diasSemana: List<String> = Literals.DiasSemana.getDaysOfWeek()
        val comidasYCenas: MyComidasYCenas = menuStore.getComidasYCenasByMenuId(menu)

        MyScrollableColumn {
            Column(Tools.styleBorderBlack) {
                Row(Tools.styleBorderBlack) {
                    HEADERS.forEach { header: String ->
                        Text(header, Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp), style = Tools.styleTableHeader)
                    }
                }
                Column {
                    diasSemana.forEachIndexed { index: Int, dia: String ->
                        Row(Tools.styleBorderBlack) {
                            Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp)) {
                                Text(dia)
                            }
                            Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp)) {
                                EditableComida(comidasYCenas, index)
                            }
                            Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp)) {
                                EditableCena(comidasYCenas, index)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EditableComida(comidasYCenas: MyComidasYCenas, index: Int) {
        if (comidasYCenas.comidas[index] == null) {
            MyIcons.AddIcon()
        }
//        else {
//            Row {
//                Text(
//                    comidasYCenas[index].comidas.toString(),
//                    Modifier.clickable {  }
//                )
//            }
//        }
    }

    @Composable
    private fun EditableCena(comidasYCenas: MyComidasYCenas, index: Int) {
        if (comidasYCenas.cenas[index] == null) {
            MyIcons.AddIcon()
        }
//        else {
//            Row {
//                Text(
//                    comidasYCenas[index].cenas.toString(),
//                    Modifier.clickable {  }
//                )
//            }
//        }
    }
}