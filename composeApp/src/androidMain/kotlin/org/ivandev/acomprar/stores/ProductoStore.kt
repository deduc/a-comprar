package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _productosByCategoria = mutableStateOf<List<ProductoEntity>?>(null)
    val productosByCategoria: State<List<ProductoEntity>?> = _productosByCategoria

    private val _productoToAdd = mutableStateOf(false)
    val productoToAdd: State<Boolean> = _productoToAdd

    private val _productoEntityToEdit = mutableStateOf<ProductoEntity?>(null)
    val productoEntityToEdit: State<ProductoEntity?> = _productoEntityToEdit

    private val _productoEntityToDelete = mutableStateOf<ProductoEntity?>(null)
    val productoEntityToDelete: State<ProductoEntity?> = _productoEntityToDelete

    private val _showAddProductoPopup = mutableStateOf(false)
    val showAddProductoPopup: State<Boolean> = _showAddProductoPopup

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllProductosByCategoria()
        }
    }


    fun addProducto(producto: Producto) {
        val added = Database.addProducto(producto)

        if (added) {
            getProductosByCategoriaId(producto.idCategoria!!)
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
        val productToUpdate: ProductoEntity? = _productosByCategoria.value?.find { it.id == productoEntity.id }

        if (productToUpdate == null) {
            println("ERROR INESPERADO intentando actualizar producto by id.")
            return
        }

        if (productToUpdate.idCategoria == productoEntity.idCategoria) {
            val updated = Database.updateProductoById(productoEntity)

            if (updated) {
                _productosByCategoria.value = _productosByCategoria.value?.map { currentProducto ->
                    if (currentProducto.id == productoEntity.id) {
                        // Creamos una nueva instancia de Producto con los nuevos valores
                        ProductoEntity(
                            productoEntity.id,
                            productoEntity.idCategoria,
                            productoEntity.nombre,
                            productoEntity.cantidad,
                            productoEntity.marca
                        )
                    } else {
                        // Devolver el producto original si no coincide el ID (en cada iteracion)
                        currentProducto
                    }
                }
            }
        } else {
            // Si la categoría ha cambiado, lo borro de la lista
            _productosByCategoria.value = _productosByCategoria.value?.filter { it.id != productoEntity.id }
        }
    }



    fun updateProductoToAdd(newValue: Boolean) {
        _productoToAdd.value = newValue
    }

    fun updateProductoToEdit(productoEntity: ProductoEntity?) {
        _productoEntityToEdit.value = productoEntity
    }

    fun updateProductoToDelete(productoEntity: ProductoEntity?) {
        _productoEntityToDelete.value = productoEntity
    }

    fun updateShowAddProductoPopup(newValue: Boolean) {
        _showAddProductoPopup.value = newValue
    }

    private suspend fun getAllProductosByCategoria() {
        val data = Database.getAllProductosByCategoria()

        withContext(Dispatchers.Main) {
            _categoriasWithProductosList.value = data
        }
    }
}
