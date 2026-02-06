package org.ivandev.acomprar.components.popups

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun PopupDelete(
    title: String,
    text: String,
    showPopup: MutableState<Boolean>,
    onClickFn: () -> Unit
) {
    val navigator: Navigator = LocalNavigator.currentOrThrow

    AlertDialog(
        onDismissRequest = { showPopup.value = false },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = {
                onClickFn()
                navigator.popAll()
            }) {
                Text("Borrar")
            }
        },
        dismissButton = {
            Button(onClick = { showPopup.value = false }) {
                Text("Cancelar")
            }
        }
    )
}
