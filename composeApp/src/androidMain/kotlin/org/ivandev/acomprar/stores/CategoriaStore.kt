package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.models.Categoria

class CategoriaStore : ViewModel() {
    // valor modificable
    private val _categorias = mutableStateOf(Database.getAllCategoria())
    // valor para obtener
    val categorias: State<List<CategoriaEntity>> = _categorias

    private val _categoriaEntityToDelete = mutableStateOf<CategoriaEntity?>(null)
    val categoriaEntityToDelete: State<CategoriaEntity?> = _categoriaEntityToDelete

    private val _categoriaEntityToEdit = mutableStateOf<CategoriaEntity?>(null)
    val categoriaEntityToEdit: State<CategoriaEntity?> = _categoriaEntityToEdit

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _categorias.value = Database.getAllCategoria()
        }
    }

    fun updateCategoria(updatedCategoriaEntity: CategoriaEntity) {
        _categorias.value = _categorias.value.map {
            if (it.id == updatedCategoriaEntity.id) updatedCategoriaEntity else it
        }.toList()

        Database.updateCategoriaById(updatedCategoriaEntity)
    }

    fun addCategoria(categoria: Categoria) {
        val added = Database.addCategoria(categoria)

        if (added) {
            val newId = _categorias.value.last().id + 1
            _categorias.value += CategoriaEntity(newId, categoria.nombre)
        }

    }

    fun deleteCategoria(deleteCategoriaEntity: CategoriaEntity) {
        Database.deleteCategoriaById(deleteCategoriaEntity.id)
        _categorias.value = _categorias.value.filter { it.id != deleteCategoriaEntity.id }.toList()
    }

    fun updateCategoriaToDelete(categoriaEntity: CategoriaEntity?) {
        _categoriaEntityToDelete.value = categoriaEntity
    }

    fun updateCategoriaToEdit(categoriaEntity: CategoriaEntity?) {
        _categoriaEntityToEdit.value = categoriaEntity
    }

}
