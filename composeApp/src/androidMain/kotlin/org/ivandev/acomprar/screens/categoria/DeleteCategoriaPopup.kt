package org.ivandev.acomprar.screens.categoria

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun DeleteCategoriaPopup(categoria: State<Categoria?>) {
    val categoriaStore: CategoriaStore = viewModel()

    if (categoria.value != null) {
        AlertDialog(
            onDismissRequest = { categoriaStore.updateCategoriaToDelete(null) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteCategoria(categoria.value!!, categoriaStore)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { categoriaStore.updateCategoriaToDelete(null) }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Borrar categoría") },
            text = {
                Text("¿Quieres borrar la categoría${categoria.value!!.nombre}?")
            }
        )
    }
}

private fun deleteCategoria(categoria: Categoria, categoriaStore: CategoriaStore) {
    categoriaStore.deleteCategoria(categoria)
    categoriaStore.updateCategoriaToDelete(null)
}
