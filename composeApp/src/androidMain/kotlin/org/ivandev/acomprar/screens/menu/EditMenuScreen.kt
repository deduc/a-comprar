package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas
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

        MenuFormulary(menuStore, menu)
    }

    @Composable
    fun MenuFormulary(menuStore: MenuStore, menu: Menu) {
        val headers: List<String> = listOf(Literals.Table.DIA_COLUMN, Literals.Table.COMIDA_COLUMN, Literals.Table.CENA_COLUMN)
        val diasSemana: List<String> = Literals.DaysOfWeek.getDaysOfWeek()
        val comidasYCenas = remember { mutableStateOf<MyMenuComidas?>(null) }
        var indexAux: Int = 0

        // LaunchedEffect carga datos de manera reactiva y la UI se refresca (y depende de) cuando menu.id cambia
        LaunchedEffect(menu.id) {
            comidasYCenas.value = menuStore.getComidasYCenasByMenuId(menu)
        }

        if (comidasYCenas.value != null) {
            MyScrollableColumn {
                Column(Tools.styleBorderBlack) {
                    TableHeaders(headers)

                    // Mostrar las comidas y cenas por cada día de la semana
                    Column {
                        diasSemana.forEachIndexed { index: Int, dia: String ->
                            Row(Tools.styleBorderBlack) {
                                Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp).fillMaxSize()) {
                                    Text(dia)
                                }

                                Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp).fillMaxSize()) {
                                    EditableComida(comidasYCenas.value!!, indexAux)
                                }

                                indexAux++

                                Column(Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp).fillMaxSize()) {
                                    EditableComida(comidasYCenas.value!!, indexAux)
                                }

                                indexAux++
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
                Text(header, Modifier.weight(1f).border(1.dp, Color.Black).padding(8.dp), style = Tools.styleTableHeader)
            }
        }
    }

    @Composable
    fun EditableComida(comidasYCenas: MyMenuComidas, index: Int) {
        if (comidasYCenas.comidas.size <= index) {
            Row {
                MyIcons.AddIcon()
                Text("HOLA")
            }
        }
        else {
            Row {
                Text(
                    comidasYCenas.comidas[index]?.nombre ?: "aaa",
                    Modifier.clickable {  }
                )
            }
        }
    }
}