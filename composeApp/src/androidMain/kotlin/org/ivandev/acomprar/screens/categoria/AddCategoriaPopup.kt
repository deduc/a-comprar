package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
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
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.stores.CategoriaStore

@Composable
fun AddCategoriaPopup() {
    val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    var nombreCategoria by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { categoriaStore.setShowAddPopup(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    val newCategoria = Categoria(null, nombreCategoria)
                    categoriaStore.addCategoria(newCategoria)
                    categoriaStore.setShowAddPopup(false)
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = { categoriaStore.setShowAddPopup(false) }) {
                Text("Cancelar")
            }
        },
        title = { Text("Añadir categoría") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = nombreCategoria,
                    onValueChange = { nombreCategoria = it },
                    label = { Text("Nombre de la categoría") }
                )
            }
        }
    )
}