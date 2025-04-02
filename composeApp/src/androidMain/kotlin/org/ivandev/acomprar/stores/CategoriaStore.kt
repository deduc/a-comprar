package org.ivandev.acomprar.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria

class CategoriaStore : ViewModel() {
    // valor modificable
    private val _categorias = mutableStateOf<List<Categoria>>(Database.getAllCategoria())
    // valor para obtener
    val categorias: State<List<Categoria>> = _categorias

    private val _showPopupDelete = mutableStateOf<Boolean>(false)
    val showPopupDelete: State<Boolean> = _showPopupDelete

    private val _categoriaToDelete = mutableStateOf<Categoria?>(null)
    val categoriaToDelete: State<Categoria?> = _categoriaToDelete

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _categorias.value = Database.getAllCategoria()
        }
    }

    fun updateCategoria(updatedCategoria: Categoria) {
        _categorias.value = _categorias.value.map {
            if (it.id == updatedCategoria.id) updatedCategoria else it
        }
    }

    fun addCategoria(newCategoria: Categoria) {
        val lastId = _categorias.value.last().id!!
        newCategoria.id = lastId + 1
        _categorias.value = _categorias.value + newCategoria
    }

    fun deleteCategoria(deleteCategoria: Categoria) {
        Database.deleteCategoriaById(deleteCategoria.id!!)
        _categorias.value = _categorias.value.filter { it.id != deleteCategoria.id }

    }

    fun updateShowPopupDelete(newState: Boolean) {
        _showPopupDelete.value = newState
    }
}
