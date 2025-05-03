package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.screens.menu.classes.SelectedDayOfWeek
import org.ivandev.acomprar.stores.MenuStore

@Composable
fun AddMenuPopup(onDismiss: () -> Unit) {
    val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    var menuName = remember { mutableStateOf("") }

    if (menuStore.showAddMenuPopup.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        menuStore.addMenu(Menu(null, menuName.value))
                        menuStore.initializeCheckedList()
                        onDismiss()
                    }
                ) {
                    Text(Literals.ButtonsText.ADD_MENU)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(Literals.ButtonsText.CANEL_ACTION)
                }
            },
            text = {
                Column {
                    Text(Literals.ADD_MENU_TITLE, style = Tools.styleTitle)
                    Spacer(Tools.spacer8dpHeight)

                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Column {
                            TextField(
                                value = menuName.value,
                                onValueChange = { menuName.value = it },
                                label = { Text("Nombre del menú") }
                            )
                        }

                        Column {
                            DaysOfWeekFormulary(menuStore)
                        }

                        Column {
                            ActionButtons(menuStore)
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun DaysOfWeekFormulary(menuStore: MenuStore) {
    val selectedDayOfWeek: MutableList<SelectedDayOfWeek> = mutableListOf()
    val checkedList = menuStore.checkedList
    val rows = menuStore.daysOfWeek.chunked(2)

    Column {
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowItems.forEachIndexed { columnIndex, day ->
                    val index = rowIndex * 2 + columnIndex
                    val checkedState = checkedList[index]

                    Row(
                        Modifier.weight(1f).padding(8.dp).clickable { checkedState.value = !checkedState.value },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it }
                        )
                        Text(text = day)
                    }

                    // Si es una fila impar (por ejemplo 7 días), agregamos un espacio vacío
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtons(menuStore: MenuStore) {
    var daysOfWeek = menuStore.daysOfWeek
    var checkedList = menuStore.checkedList

    var allDaysClicked = remember { mutableStateOf(false) }
    var diarioClicked = remember { mutableStateOf(false) }
    var weekendClicked = remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = {
            allDaysClicked.value = !allDaysClicked.value
            diarioClicked.value = false
            weekendClicked.value = false

            checkedList.map {
                if (! allDaysClicked.value) it.value = false
                else it.value = true
            }
        }) {
            Text(if(! allDaysClicked.value) "Todos" else "Ninguno")
        }

        Button(onClick = {
            allDaysClicked.value = false
            diarioClicked.value = !diarioClicked.value
            weekendClicked.value = false

            checkedList.forEachIndexed { index: Int, day: MutableState<Boolean> ->
                if (index < daysOfWeek.size-2) {
                    if(day.value) day.value = false
                    else day.value = true
                }
            }
        }) {
            Text("Diario")
        }

        Button(onClick = {
            allDaysClicked.value = false
            diarioClicked.value = !diarioClicked.value
            weekendClicked.value = false

            val last = checkedList.size - 1
            checkedList[last].value = true
            checkedList[last - 1].value = true

        }) {
            Text("Finde")
        }
    }
}