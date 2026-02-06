package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum
import org.ivandev.acomprar.stores.special_classes.MainCarritoState

class MainCarritoStore: ViewModel() {
    private val _mainCarritoState = mutableStateOf(MainCarritoState())
    val mainCarritoState: State<MainCarritoState> = _mainCarritoState

    fun addCarritoToMainCarrito(id: Int) {
        viewModelScope.launch {
            val added = withContext(Dispatchers.IO) {
                Database.addCarritoToMainCarrito(id)
            }

            if (added) {
                Tools.Notifier.showToast(Literals.ToastText.ADDED_CARRITO_TO_MAIN_CARRITO)
                inmutableAddIdCarritoToMainCarrito(id)
            }
            else
                Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_CARRITO_TO_MAIN_CARRITO)
        }
    }
    fun removeCarritoToMainCarrito(id: Int) {
        deleteCarritoFromMainCarrito(id)
        inmutableRemoveIdCarritoToMainCarrito(id)
    }

    fun getAllCarrito() {
        viewModelScope.launch {
            val carritosAux: List<CarritoEntity> = withContext(Dispatchers.IO) {
                Database.getCarritosAddedToMainCarrito()
            }

            inmutableUpdateCarritos(carritosAux)
        }
    }
    fun getCarritosAddedToMainCarrito() {
        viewModelScope.launch {
            val carritosAux: List<CarritoEntity> = withContext(Dispatchers.IO) {
                Database.getCarritosAddedToMainCarrito()
            }

            inmutableUpdateCarritos(carritosAux)

            carritosAux.forEach {
                inmutableAddIdCarritoToMainCarrito(it.id)
            }
        }
    }

    fun deleteCarritoFromMainCarrito(id: Int) {
        viewModelScope.launch {
            val deleted = withContext(Dispatchers.IO) {
                Database.deleteCarritoFromMainCarrito(id)
            }

            if (deleted) {
                Tools.Notifier.showToast(Literals.ToastText.DELETED_CARRITO_FROM_MAIN_CARRITO)
                deleteCarritoById(id)
            }
            else
                Tools.Notifier.showToast(Literals.ToastText.ERROR_DELETING_CARRITO_FROM_MAIN_CARRITO)
        }
    }

    fun knowIfUserIsBuying() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                Database.getUserActionByActionType(Literals.UserActions.USER_BUYING)
            }
            _mainCarritoState.value = mainCarritoState.value.copy(
                userBuying = result
            )
        }
    }

    fun stopBuying(boughtProducts: List<ProductoEntity>, notBoughtProducts: List<ProductoEntity>) {
        setShowStopBuyingPopup(false)
        setUserIsBuying(UserBuyingEnum.USER_IS_NOT_BUYING)

        viewModelScope.launch {
            val result1 = withContext(Dispatchers.IO) {
                Database.deleteBoughtProductsFromMainCarrito(boughtProducts)
            }
            val result3 = withContext(Dispatchers.IO) {
                Database.deleteFromCarritoBastardo()
            }
            val result4 = withContext(Dispatchers.IO) {
                try {
                    Database.deleteCarritosFromMainCarrito()
                    val carritosAux: List<CarritoEntity> = _mainCarritoState.value.carritos.filter {
                        it.id != Literals.Database.HardcodedValues.CARRITO_BASTARDO_ID
                    }
                    carritosAux.forEach { deleteCarritoById(it.id) }
                    true
                } catch (e: Exception) {
                    println("[DEBUG] Error al borrar los carritos del carrito bastardo $e")
                    false
                }
            }
            val result2 = withContext(Dispatchers.IO) {
                if (notBoughtProducts.isNotEmpty())
                    Database.addNotBoughtProductsIntoSpecialCarrito(notBoughtProducts)
                else true
            }

            if (result1 && result2 && result3 && result4) {
                _mainCarritoState.value = mainCarritoState.value.copy(
                    stoppedBuying = true
                )
            }
            else {
                Tools.Notifier.showToast("Error desconocido borrando mierda $result1 - $result2 - $result3")
            }
        }
    }

    fun setUserIsBuying(value: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Database.setUserIsBuying(value)
            }

            val userBuyingAux: UserActionsEntity = _mainCarritoState.value.userBuying
            userBuyingAux.actionValue = value

            _mainCarritoState.value = _mainCarritoState.value.copy(
                userBuying = userBuyingAux
            )
        }
    }

    fun setStoppedBuying(value: Boolean) {
        _mainCarritoState.value = _mainCarritoState.value.copy(
            stoppedBuying = value
        )
    }

    fun setShowAComprarPopup(value: Boolean) {
        _mainCarritoState.value = _mainCarritoState.value.copy(
            showAComprarPopup = value
        )
    }
    fun setShowStopBuyingPopup(value: Boolean) {
        _mainCarritoState.value = _mainCarritoState.value.copy(
            showStopBuyingPopup = value
        )
    }

    fun checkUserBuying(): Boolean {
        return mainCarritoState.value.userBuying.actionType == Literals.UserActions.USER_BUYING &&
                mainCarritoState.value.userBuying.actionValue == UserBuyingEnum.USER_IS_BUYING
    }

    fun loadAndInsertCarritosToBuyList() {
        viewModelScope.launch {
            var carritosToBuy = withContext(Dispatchers.IO) {
                Database.loadAndInsertCarritosToBuyList()
            }

            carritosToBuy = carritosToBuy.filter { it.productoEntities.isNotEmpty() }

            _mainCarritoState.value = mainCarritoState.value.copy(
                carritosToBuy = carritosToBuy
            )
        }
    }

    fun loadCarritosToBuyList() {
        viewModelScope.launch {
            var carritosToBuy = withContext(Dispatchers.IO) {
                Database.loadCarritosToBuyList()
            }

            carritosToBuy = carritosToBuy.filter { it.productoEntities.isNotEmpty() }

            _mainCarritoState.value = mainCarritoState.value.copy(
                carritosToBuy = carritosToBuy
            )
        }
    }

    private fun inmutableAddIdCarritoToMainCarrito(id: Int) {
        val newList: MutableList<Int> = _mainCarritoState.value.idCarritosAddedToMainCarrito.toMutableList()
        newList.add(id)

        _mainCarritoState.value = _mainCarritoState.value.copy(
            idCarritosAddedToMainCarrito = newList.toList()
        )
    }

    private fun inmutableRemoveIdCarritoToMainCarrito(id: Int) {
        val newList: List<Int> = _mainCarritoState.value.idCarritosAddedToMainCarrito.filter { it != id }

        _mainCarritoState.value = _mainCarritoState.value.copy(
            idCarritosAddedToMainCarrito = newList
        )
    }


    private fun inmutableUpdateCarritos(newValue: List<CarritoEntity>) {
        // Actualización inmutable
        _mainCarritoState.value = _mainCarritoState.value.copy(
            carritos = newValue
        )
    }
    private fun deleteCarritoById(id: Int) {
        val copy = mainCarritoState.value.carritos.filter { it.id != id }
        inmutableUpdateCarritos(copy)
    }
}
