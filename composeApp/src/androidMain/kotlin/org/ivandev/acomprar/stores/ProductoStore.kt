package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Producto

class ProductoStore : ViewModel() {
    private var _categoriasWithProductosList = mutableStateOf<List<CategoriaWithProductos>?>(null)
    val categoriasWithProductosList: State<List<CategoriaWithProductos>?> = _categoriasWithProductosList

    // Productos filtered by IdCategoria
    private var _productosByCategoria = mutableStateListOf<ProductoEntity>()
    val productosByCategoria: SnapshotStateList<ProductoEntity> = _productosByCategoria

    private var _productoToAdd = mutableStateOf<Producto?>(null)
    val productoToAdd: State<Producto?> = _productoToAdd


    // popups
    private var _addProductoPopup = mutableStateOf(false)
    val addProductoPopup: State<Boolean> = _addProductoPopup

    private var _editProductoEntityPopup = mutableStateOf<ProductoEntity?>(null)
    val editProductoEntityPopup: State<ProductoEntity?> = _editProductoEntityPopup

    private var _deleteProductoEntityPopup = mutableStateOf<ProductoEntity?>(null)
    val deleteProductoEntityPopup: State<ProductoEntity?> = _deleteProductoEntityPopup

    private var _showAddProductoPopup = mutableStateOf(false)
    val showAddProductoPopup: State<Boolean> = _showAddProductoPopup

    private var _showEditProductoPopup = mutableStateOf(false)
    val showEditProductoPopup: State<Boolean> = _showEditProductoPopup

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadProductosPorCategoria()
        }
    }

    private fun isProductoOk(idCategoria: Int?, nombre: String): Boolean {
        var message = ""

        if (idCategoria == null) message += Literals.ToastText.ERROR_NULL_ID_CATEGORIA
        if (nombre.isEmpty()) message += Literals.ToastText.ERROR_PRODUCTO_NAME

        if (message.isNotEmpty()) {
            Tools.Notifier.showToast(message)
            return false
        }
        else return true
    }

    fun addNewProducto(idCategoria: Int?, nombre: String, cantidad: String, marca: String) {
        if (isProductoOk(idCategoria, nombre)) {
            val newProducto = Producto(
                id = null,
                idCategoria = idCategoria,
                nombre = nombre,
                cantidad = cantidad,
                marca = marca
            )
            addProducto(newProducto)
            setAddProductoPopup(false)
            setShowAddProductoPopup(false)
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
                _productosByCategoria.clear()
                _productosByCategoria.addAll(result)
            }
        }
    }

    suspend fun getCategoriaIdByProductoId(producto: Producto): Int {
        return withContext(Dispatchers.IO) {
            Database.getCategoriaIdByProductoId(producto)
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

    fun setShowAddProductoPopup(newValue: Boolean) {
        _showAddProductoPopup.value = newValue
    }

    fun setShowEditProductoPopup(newValue: Boolean) {
        _showEditProductoPopup.value = newValue
    }

    fun deleteProductoEntity(productoEntity: ProductoEntity) {
        Database.deleteProductoById(productoEntity.id)

        CoroutineScope(Dispatchers.IO).launch {
            loadProductosPorCategoria()
        }
    }

    private fun loadProductosPorCategoria() {
        viewModelScope.launch(Dispatchers.IO) {
            val productosPorCategoria = Database.getAllProductosByCategoria()

            // actualizar desde el hilo Main el valor de _categoriasWithProductosList
            withContext(Dispatchers.Main) {
                _categoriasWithProductosList.value = productosPorCategoria
            }
        }
    }
}
