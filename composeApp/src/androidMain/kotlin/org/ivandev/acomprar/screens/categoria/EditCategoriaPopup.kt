package org.ivandev.acomprar.screens.categoria

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun EditCategoriaPopup(categoriaToEdit: State<Categoria?>) {
    val categoriaStore: CategoriaStore = viewModel()

    var newCategoriaName by remember { mutableStateOf(categoriaToEdit.value!!.nombre) }

    if (categoriaToEdit.value != null) {
        AlertDialog(
            onDismissRequest = { categoriaStore.updateCategoriaToEdit(null) },
            confirmButton = {
                TextButton(
                    onClick = {
                        categoriaToEdit.value!!.nombre = newCategoriaName
                        categoriaStore.updateCategoria(categoriaToEdit.value!!)
                        categoriaStore.updateCategoriaToEdit(null)
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