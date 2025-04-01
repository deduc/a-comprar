package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.stores.MenuStore

@Composable
fun AddMenuPopup(onDismiss: () -> Unit) {
    val menuStore: MenuStore = viewModel()
    var menuName = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    menuStore.addMenu(Menu(null, menuName.value))
                    onDismiss()
                }
            ) { Text(Literals.ADD_TEXT) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }

        },
        title = { Text(Literals.ADD_MENU_TITLE) },
        text = {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(
                    value = menuName.value,
                    onValueChange = { menuName.value = it },
                    label = { Text("Nombre del menú") }
                )
            }
        }
    )
}