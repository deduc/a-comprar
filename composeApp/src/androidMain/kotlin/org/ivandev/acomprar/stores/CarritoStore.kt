package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CarritoEntity
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


    fun addCarrito() {
        _newCarritoToAdd.value = Carrito(null, _carritoName.value, _carritoDescription.value)

        if (Database.addCarrito(_newCarritoToAdd.value!!)) Tools.Notifier.showToast("Carrito añadido.")
        else Tools.Notifier.showToast("ERROR al añadir el carrito.")

        setShowAddCarritoPopup(false)
        getAllCarrito()
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

    fun getCarritoAndProductosByCarritoId(id: Int) {
        Database.getCarritoAndProductosByCarritoId(id)
    }

    fun setShowAddCarritoPopup(newValue: Boolean) {
        _showAddCarritoPopup.value = newValue
    }
}