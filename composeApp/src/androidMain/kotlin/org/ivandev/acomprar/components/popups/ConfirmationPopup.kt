package org.ivandev.acomprar.components.popups

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ConfirmationPopup(
    text: String = "Confirmar acción",
    onAcceptMethod: () -> Unit,
    onDismiss: () -> Unit
) {
    var show = remember { mutableStateOf(true) }

    if (show.value) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                show.value = false
            },
            text = { Text(text) },
            confirmButton = {
                Button(onClick = {
                    onAcceptMethod()
                    onDismiss()
                    show.value = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDismiss()
                    show.value = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
