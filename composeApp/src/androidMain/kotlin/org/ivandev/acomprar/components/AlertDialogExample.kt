package org.ivandev.acomprar.components

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogExample() {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = { }) {
                Text("Cancelar")
            }
        },
        title = { },
        text = { },
    )
}
