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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.stores.MenuStore

@Composable
fun AddMenuPopup() {
    val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    var menuName = remember { mutableStateOf("") }
    val checkedList: SnapshotStateList<MutableState<Boolean>> = menuStore.checkedList

    LaunchedEffect(menuStore.showAddMenuPopup.value) {
        if (! menuStore.showAddMenuPopup.value) {
            menuStore.setShowAddMenuPopup(false)
        }
    }

    if (menuStore.showAddMenuPopup.value) {
        AlertDialog(
            onDismissRequest = {
                menuStore.deleteCheckedData()
                menuStore.setShowAddMenuPopup(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val menu = Menu(null, menuName.value)
                        menuStore.onConfirmAddMenu(menu, checkedList)
                    }
                ) {
                    Text(Literals.ButtonsText.ADD_MENU)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    menuStore.deleteCheckedData()
                    menuStore.setShowAddMenuPopup(false)
                }) {
                    Text(Literals.ButtonsText.CANEL_ACTION)
                }
            },
            title = { Text(Literals.ADD_MENU_TITLE, style = Tools.styleTitleUnderlineBlack) },
            text = {
                Column {
                    Spacer(Tools.spacer8dpHeight)

                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            value = menuName.value,
                            onValueChange = { menuName.value = it },
                            label = { Text("Nombre del menú") }
                        )

                        DaysOfWeekFormulary(menuStore, checkedList)
                        SelectionDaysButtons(menuStore)
                    }
                }
            }
        )
    }
}

@Composable
private fun DaysOfWeekFormulary(menuStore: MenuStore, checkedList: SnapshotStateList<MutableState<Boolean>>) {
    val rows = menuStore.daysOfWeek.chunked(2)

    Column {
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowItems.forEachIndexed { columnIndex, day ->
                    val index = rowIndex * 2 + columnIndex
                    val checkedState = checkedList[index]

                    Row(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable { checkedState.value = !checkedState.value },
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
fun SelectionDaysButtons(menuStore: MenuStore) {
    var daysOfWeek = menuStore.daysOfWeek
    var checkedList = menuStore.checkedList

    var allDaysClicked = remember { mutableStateOf(false) }
    var diarioClicked = remember { mutableStateOf(false) }
    var weekendClicked = remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
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

            val lastDayValid = daysOfWeek.size - 2
            checkedList.forEachIndexed { index: Int, day: MutableState<Boolean> ->
                if (index < lastDayValid) {
                    if(diarioClicked.value) day.value = true
                    else day.value = false
                }
                else {
                    day.value = false
                }
            }
        }) {
            Text("Diario")
        }

        Button(onClick = {
            allDaysClicked.value = false
            diarioClicked.value = false
            weekendClicked.value = !weekendClicked.value

            val lastDayValid = daysOfWeek.size - 2
            checkedList.forEachIndexed { index: Int, day: MutableState<Boolean> ->
                if (index < lastDayValid) {
                    day.value = false
                }
                else {
                    day.value = weekendClicked.value
                }
            }
        }) {
            Text("Finde")
        }
    }
}