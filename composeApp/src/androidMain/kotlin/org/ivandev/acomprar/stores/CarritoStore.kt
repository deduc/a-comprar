package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.models.Carrito

class CarritoStore: ViewModel() {
    private var _carritos = mutableStateListOf<CarritoEntity>()
    val carritos: SnapshotStateList<CarritoEntity> = _carritos

    private var _showAddCarritoPopup = mutableStateOf<Boolean>(false)
    val showAddCarritoPopup: State<Boolean> = _showAddCarritoPopup

    private var _newCarritoToAdd = mutableStateOf<Carrito?>(null)
    val newCarritoToAdd: State<Carrito?> = _newCarritoToAdd

    var _carritoName = mutableStateOf<String>("")
    var _carritoDescription = mutableStateOf<String>("")

    private var _carritoAndProductos = mutableStateOf<CarritoAndProductsData?>(null)
    val carritoAndProductos: State<CarritoAndProductsData?> = _carritoAndProductos


    fun addCarrito() {
        _newCarritoToAdd.value = Carrito(null, _carritoName.value, _carritoDescription.value)

        if (Database.addCarrito(_newCarritoToAdd.value!!)) Tools.Notifier.showToast("Carrito añadido.")
        else Tools.Notifier.showToast("ERROR al añadir el carrito.")

        setShowAddCarritoPopup(false)
        getAllCarrito()

        _carritoName.value = ""
        _carritoDescription.value = ""
    }

    fun addProductoToCurrentCarrito(producto: ProductoEntity) {
        val currentCarritoAndProductos: CarritoAndProductsData? = _carritoAndProductos.value
        val cantidad: Int = 1

        if (currentCarritoAndProductos != null) {
            // Añadir producto y cantidad a la lista mutable
            currentCarritoAndProductos.productosAndCantidades.add(producto to cantidad)

            // Forzar recomposición en Compose (opcional si necesitas que se actualice la UI)
            _carritoAndProductos.value = currentCarritoAndProductos

            viewModelScope.launch {
                val added = withContext(Dispatchers.IO) {
                    Database.addProductoToCurrentCarrito(currentCarritoAndProductos.carrito, producto)
                }

                if (added) {
                    Tools.Notifier.showToast(Literals.ToastText.ADDED_PRODUCTO)
                }
            }
        }
        else {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_PRODUCTO_TO_UNKNOWN_CARRITO)
        }
    }


    fun deleteCarritoById(id: Int) {
        if (Database.deleteCarritoById(id)) Tools.Notifier.showToast("Carrito borrado.")
        else Tools.Notifier.showToast("ERROR desconocido al intentar borrar el carrito.")

        getAllCarrito()
    }

    fun getAllCarrito(): SnapshotStateList<CarritoEntity> {
        _carritos.clear()
        _carritos.addAll(Database.getAllCarrito())

        return carritos
    }

    // de esta manera solo hay que poner LaunchedEffect { xStore.funcion() }
    suspend fun getCarritoAndProductosByCarritoId(id: Int) {
        withContext(Dispatchers.IO) {
            _carritoAndProductos.value = Database.getCarritoAndProductosByCarritoId(id)
        }
    }

    fun setShowAddCarritoPopup(newValue: Boolean) {
        _showAddCarritoPopup.value = newValue
    }
}