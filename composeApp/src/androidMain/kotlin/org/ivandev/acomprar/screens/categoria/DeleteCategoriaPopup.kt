package org.ivandev.acomprar.screens.categoria

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.viewModels.CategoriaStore

@Composable
fun DeleteCategoriaPopup(categoriaEntity: State<CategoriaEntity?>) {
    val categoriaStore: CategoriaStore = viewModel()

    if (categoriaEntity.value != null) {
        AlertDialog(
            onDismissRequest = { categoriaStore.updateCategoriaToDelete(null) },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteCategoria(categoriaEntity.value!!, categoriaStore)
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
                Text("¿Quieres borrar la categoría ${categoriaEntity.value!!.nombre}?")
            }
        )
    }
}

private fun deleteCategoria(categoriaEntity: CategoriaEntity, categoriaStore: CategoriaStore) {
    categoriaStore.deleteCategoria(categoriaEntity)
    categoriaStore.updateCategoriaToDelete(null)
}
