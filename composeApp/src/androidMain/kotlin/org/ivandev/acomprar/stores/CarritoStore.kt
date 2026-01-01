package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

    private var _editingCarrito = mutableStateOf<CarritoEntity?>(null)
    val editingCarrito: State<CarritoEntity?> = _editingCarrito

    private var _showAddCarritoPopup = mutableStateOf<Boolean>(false)
    val showAddCarritoPopup: State<Boolean> = _showAddCarritoPopup
    private var _showEditCarritoPopup = mutableStateOf<Boolean>(false)
    val showEditCarritoPopup: State<Boolean> = _showEditCarritoPopup


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

                if (!added) Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_PRODUCTO)
            }
        }
        else {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_PRODUCTO_TO_UNKNOWN_CARRITO)
        }
    }

    fun substractProductoToCurrentCarrito(producto: ProductoEntity) {
        val currentCarritoAndProductos: CarritoAndProductsData? = _carritoAndProductos.value
        val cantidad: Int = -1

        if (currentCarritoAndProductos != null) {
            // Añadir producto y cantidad a la lista mutable
            currentCarritoAndProductos.productosAndCantidades.add(producto to cantidad)

            // Forzar recomposición en Compose (opcional si necesitas que se actualice la UI)
            _carritoAndProductos.value = currentCarritoAndProductos

            viewModelScope.launch {
                val added = withContext(Dispatchers.IO) {
                    Database.substractProductoToCurrentCarrito(currentCarritoAndProductos.carrito, producto)
                }

                if (added && cantidad == 1) {
                    Tools.Notifier.showToast(Literals.ToastText.ADDED_PRODUCTO)
                }
                else if (added && cantidad == -1) {
                    Tools.Notifier.showToast(Literals.ToastText.ADDED_PRODUCTO)
                }
            }
        }
        else {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_PRODUCTO_TO_UNKNOWN_CARRITO)
        }
    }

    fun doFixCantidadStr(str: String?, cantidad: Int): String {
        if (str == null)
            return ""

        val regex = Regex("""^(\d+(?:[.,]\d+)?)(\s*\D.*)?$""")
        val match = regex.find(str.trim())

        return if (match != null) {
            val numberStr = match.groupValues[1].replace(",", ".")
            val unidad = match.groupValues.getOrNull(2)?.trimStart() ?: ""
            val number = numberStr.toDoubleOrNull() ?: return str
            val total = number * cantidad

            val totalStr = if (numberStr.contains('.')) "%.2f".format(total) else total.toInt().toString()
            "$totalStr$unidad"
        } else {
            str
        }
    }

    fun deleteCarritoById(id: Int) {
        if (Database.deleteCarritoById(id)) Tools.Notifier.showToast("Carrito borrado.")
        else Tools.Notifier.showToast("ERROR desconocido al intentar borrar el carrito.")

        getAllCarrito()
    }

    fun getAllCarrito(): SnapshotStateList<CarritoEntity> {
        _carritos.clear()

        var carritosAux: List<CarritoEntity> = Database.getAllCarrito()
        carritosAux = carritosAux.filter {it.id != 1}

        _carritos.addAll(carritosAux)

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
    fun setShowEditCarritoPopup(newValue: Boolean) {
        _showEditCarritoPopup.value = newValue
    }
    fun setEditingCarrito(carritoEntity: CarritoEntity) {
        _editingCarrito.value = carritoEntity
    }
    fun updateCarrito() {
        val carrito = Carrito(editingCarrito.value!!.id, _carritoName.value, _carritoDescription.value)

        viewModelScope.launch(Dispatchers.IO) {
            Database.updateCarrito(carrito)
            getCarritoAndProductosByCarritoId(editingCarrito.value!!.id)
        }
    }
}