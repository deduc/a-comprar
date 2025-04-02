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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun AddCategoriaPopup() {
    val categoriaStore: CategoriaStore = viewModel()
    var showPopup by remember { mutableStateOf(true) }
    var nombreCategoria by remember { mutableStateOf("") }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newCategoria = Categoria(null, nombreCategoria)
                        addNewCategoria(newCategoria, categoriaStore)
                        showPopup = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPopup = false }) {
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
}

private fun addNewCategoria(categoria: Categoria, categoriaStore: CategoriaStore) {
    Database.addCategoria(categoria)
    categoriaStore.addCategoria(categoria)
}
