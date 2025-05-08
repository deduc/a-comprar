package org.ivandev.acomprar.screens.comida

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.models.Comida
import org.ivandev.acomprar.stores.ComidaStore

@Composable
fun AddComidaPopup() {
    val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

    var showAddComidaPopup by remember { mutableStateOf(comidaStore.showAddComidaPopup) }
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf<Int>(0) }
    var tipoCheckBoxChecked = remember { mutableStateOf(true) }

    if (showAddComidaPopup.value) {
        AlertDialog(
            onDismissRequest = { comidaStore.setShowAddComidaPopup(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        tipo = if (tipoCheckBoxChecked.value) 1 else 2
                        var comida = Comida(null, nombre, tipo)
                        Database.addComida(comida)
                        comidaStore.setShowAddComidaPopup(false)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { comidaStore.setShowAddComidaPopup(false) }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Añadir comida") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre de la comida") }
                    )

                    Row(Modifier.clickable { tipoCheckBoxChecked.value = !tipoCheckBoxChecked.value }) {
                        Column {
                            Checkbox(
                                checked = false,
                                onCheckedChange = { tipoCheckBoxChecked.value = !tipoCheckBoxChecked.value},
                                enabled = true,
                            )
                        }

                        Column {
                            var texto = if(tipoCheckBoxChecked.value) "Día" else "Noche"
                            Text(texto)
                        }
                    }
                }
            }
        )
    }
}