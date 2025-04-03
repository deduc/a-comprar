package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos

class ProductoStore : ViewModel() {
    private val _categoriaWithProductos = mutableStateOf<List<CategoriaWithProductos>?>(null)
    val categoriaWithProductos: State<List<CategoriaWithProductos>?> = _categoriaWithProductos

    private val _productoToAdd = mutableStateOf(false)
    val productoToAdd: State<Boolean> = _productoToAdd

    private val _productoToEdit = mutableStateOf<Producto?>(null)
    val productoToEdit: State<Producto?> = _productoToEdit

    private val _productoToDelete = mutableStateOf<Producto?>(null)
    val productoToDelete: State<Producto?> = _productoToDelete

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val data = Database.getAllProductosByCategoria()

            withContext(Dispatchers.Main) {
                _categoriaWithProductos.value = data
            }
        }
    }

    fun updateProductoToAdd(newValue: Boolean) {
        _productoToAdd.value = newValue
    }

    fun updateProductoToEdit(producto: Producto?) {
        _productoToEdit.value = producto
    }

    fun updateProductoToDelete(producto: Producto?) {
        _productoToDelete.value = producto
    }
}
