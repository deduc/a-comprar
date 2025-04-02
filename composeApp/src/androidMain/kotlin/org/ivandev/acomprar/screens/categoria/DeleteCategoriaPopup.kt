package org.ivandev.acomprar.screens.categoria

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun DeleteCategoriaPopup(categoria: Categoria) {
    val categoriaStore: CategoriaStore = viewModel()
    val showPopup = categoriaStore.showPopupDelete

    if (showPopup.value) {
        AlertDialog(
            onDismissRequest = { categoriaStore.updateShowPopupDelete(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteCategoria(categoria, categoriaStore)
                        categoriaStore.updateShowPopupDelete(false)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { categoriaStore.updateShowPopupDelete(false) }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Borrar categoría") },
            text = {
                Text("¿Quieres borrar la categoría${categoria.nombre}?")
            }
        )
    }
}

private fun deleteCategoria(categoria: Categoria, categoriaStore: CategoriaStore) {
    Database.deleteCategoriaById(categoria.id!!)
    categoriaStore.deleteCategoria(categoria)
}
