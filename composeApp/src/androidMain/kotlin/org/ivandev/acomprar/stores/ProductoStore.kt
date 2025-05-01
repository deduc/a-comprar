package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Producto

class ProductoStore : ViewModel() {
    private val _categoriasWithProductosList = mutableStateOf<List<CategoriaWithProductos>?>(null)
    val categoriasWithProductosList: State<List<CategoriaWithProductos>?> = _categoriasWithProductosList

    // Productos filtered by IdCategoria
    private val _productosByCategoria = mutableStateOf<List<ProductoEntity>?>(null)
    val productosByCategoria: State<List<ProductoEntity>?> = _productosByCategoria

    private val _productoToAdd = mutableStateOf<Producto?>(null)
    val productoToAdd: State<Producto?> = _productoToAdd


    // popups
    private val _addProductoPopup = mutableStateOf(false)
    val addProductoPopup: State<Boolean> = _addProductoPopup

    private val _editProductoEntityPopup = mutableStateOf<ProductoEntity?>(null)
    val editProductoEntityPopup: State<ProductoEntity?> = _editProductoEntityPopup

    private val _deleteProductoEntityPopup = mutableStateOf<ProductoEntity?>(null)
    val deleteProductoEntityPopup: State<ProductoEntity?> = _deleteProductoEntityPopup

    private val _showAddProductoPopup = mutableStateOf(false)
    val showAddProductoPopup: State<Boolean> = _showAddProductoPopup


    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadProductosPorCategoria()
        }
    }


    fun addProducto(producto: Producto) {
        val added = Database.addProducto(producto)

        if (added) {
            getProductosByCategoriaId(producto.idCategoria!!)

            CoroutineScope(Dispatchers.IO).launch {
                loadProductosPorCategoria()
            }
        }
    }

    fun getProductosByCategoriaId(idCategoria: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = Database.getProductosByCategoriaId(idCategoria)

            withContext(Dispatchers.Main) {
                _productosByCategoria.value = result
            }
        }
    }

    fun updateProductoById(productoEntity: ProductoEntity) {
        val updated = Database.updateProductoById(productoEntity)

        if (updated) {
            CoroutineScope(Dispatchers.IO).launch {
                loadProductosPorCategoria()
            }
        }
    }


    fun setProductoToAdd(producto: Producto) {
        _productoToAdd.value = producto
    }

    fun setAddProductoPopup(newValue: Boolean) {
        _addProductoPopup.value = newValue
    }

    fun setEditProductoPopup(productoEntity: ProductoEntity?) {
        _editProductoEntityPopup.value = productoEntity
    }

    fun setProductoToDeletePopup(productoEntity: ProductoEntity?) {
        _deleteProductoEntityPopup.value = productoEntity
    }

    fun deleteProductoEntity(productoEntity: ProductoEntity) {
        Database.deleteProductoById(productoEntity.id)

        CoroutineScope(Dispatchers.IO).launch {
            loadProductosPorCategoria()
        }
    }

    private fun loadProductosPorCategoria() {
        viewModelScope.launch {
            val productosPorCategoria = withContext(Dispatchers.IO) {
                Database.getAllProductosByCategoria()
            }
            _categoriasWithProductosList.value = productosPorCategoria
        }
    }
}
