package org.ivandev.acomprar.screens.categoria

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun EditCategoriaPopup(categoria: Categoria) {
    val categoriaStore: CategoriaStore = viewModel()

    var showPopup by remember { mutableStateOf(true) }
    var newCategoriaName by remember { mutableStateOf(categoria.nombre) }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPopup = false

                        val updatedCategoria = categoria.apply {
                            this.nombre = newCategoriaName  // Modificamos el nombre directamente
                        }

                        categoriaStore.updateCategoria(updatedCategoria)
                        Database.updateCategoriaById(categoria)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            title = { Text("Editando categoría") },
            text = {
                TextField(
                    value = newCategoriaName,
                    onValueChange = { newCategoriaName = it },
                )
            }
        )
    }
}
