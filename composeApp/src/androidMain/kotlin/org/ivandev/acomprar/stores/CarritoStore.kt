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

    private var _editingCarrito = mutableStateOf<CarritoEntity?>(null)
    val editingCarrito: State<CarritoEntity?> = _editingCarrito

    private var _showAddCarritoPopup = mutableStateOf<Boolean>(false)
    val showAddCarritoPopup: State<Boolean> = _showAddCarritoPopup
    private var _showEditCarritoPopup = mutableStateOf<Boolean>(false)
    val showEditCarritoPopup: State<Boolean> = _showEditCarritoPopup
    private var _showDeleteCarritoPopup = mutableStateOf<Boolean>(false)
    val showDeleteCarritoPopup: State<Boolean> = _showDeleteCarritoPopup
    private var _deletingCarrito = mutableStateOf<CarritoEntity?>(null)
    val deletingCarrito: State<CarritoEntity?> = _deletingCarrito


    var _carritoName = mutableStateOf<String>("")
    var _carritoDescription = mutableStateOf<String>("")

    private var _carritoAndProductos = mutableStateOf<CarritoAndProductsData?>(null)
    val carritoAndProductos: State<CarritoAndProductsData?> = _carritoAndProductos

    private var _carritosAddedToMainCarrito = mutableStateListOf<Int>()
    val carritosAddedToMainCarrito: SnapshotStateList<Int> = _carritosAddedToMainCarrito


    fun addCarrito() {
        val carrito = createCarritoWithStateValues()

        viewModelScope.launch(Dispatchers.IO) {
            val exists = Database.checkIfCarritoExists(carrito)

            if (exists) {
                withContext(Dispatchers.Main) {
                    Tools.Notifier.showToast("Ese carrito ya existe.")
                }
                return@launch
            }

            val success = Database.addCarrito(carrito)

            withContext(Dispatchers.Main) {
                if (success)
                    Tools.Notifier.showToast("Carrito añadido.")
                else
                    Tools.Notifier.showToast("ERROR al añadir el carrito.")

                setShowAddCarritoPopup(false)
                getAllCarrito()
            }
        }
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

                if (added) Tools.Notifier.showToast(Literals.ToastText.ADDED_PRODUCTO)
                else Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_PRODUCTO)
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

    fun  getAllCarrito(): SnapshotStateList<CarritoEntity> {
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
            val a = 1
        }
    }

    fun getEditingCarritoValue() {
        viewModelScope.launch {
            if (editingCarrito.value == null) {
                val carritoBastardo = withContext(Dispatchers.IO) {
                    Database.getCarritoById(Literals.Database.HardcodedValues.CARRITO_BASTARDO_ID)
                }
                setEditingCarrito(carritoBastardo)
            }
        }
    }

    fun setShowAddCarritoPopup(newValue: Boolean) {
        _showAddCarritoPopup.value = newValue
    }
    fun setShowEditCarritoPopup(newValue: Boolean) {
        _showEditCarritoPopup.value = newValue
    }
    fun setShowDeleteCarritoPopup(newValue: Boolean) {
        _showDeleteCarritoPopup.value = newValue
    }
    fun setDeletingCarrito(carritoEntity: CarritoEntity?) {
        _deletingCarrito.value = carritoEntity
    }
    fun setEditingCarrito(carritoEntity: CarritoEntity) {
        _editingCarrito.value = carritoEntity
    }
    fun setEditingCarritoUsingId(id: Int) {
        viewModelScope.launch {
            _editingCarrito.value = withContext(Dispatchers.IO) { Database.getCarritoById(id) }
        }
    }
    fun updateCarrito() {
        val carrito = Carrito(editingCarrito.value!!.id, _carritoName.value, _carritoDescription.value)

        viewModelScope.launch(Dispatchers.IO) {
            Database.updateCarrito(carrito)
            getCarritoAndProductosByCarritoId(editingCarrito.value!!.id)
        }
    }

    fun updateCarritosAddedToMainCarrito(idCarritos: List<Int>) {
        _carritosAddedToMainCarrito.clear()
        _carritosAddedToMainCarrito.addAll(idCarritos)
    }

    fun checkIfCarritosWasAddedToMainCarrito(carritos: List<CarritoEntity>) {
        val idCarritos: List<Int> = carritos.map { it.id }

        viewModelScope.launch(Dispatchers.IO) {
            updateCarritosAddedToMainCarrito(
                Database.checkIfCarritosWasAddedToMainCarrito(idCarritos)
            )
        }
    }

    private fun createCarritoWithStateValues(): Carrito {
        val carrito = Carrito(
            id = null,
            name = _carritoName.value,
            description = _carritoDescription.value
        )
        _carritoName.value = ""
        _carritoDescription.value = ""

        return carrito
    }
}
