package org.ivandev.acomprar.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun PopupDelete(
    title: String,
    text: String,
    showPopup: MutableState<Boolean>,
    onClickFn: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showPopup.value = false },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = onClickFn) {
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
