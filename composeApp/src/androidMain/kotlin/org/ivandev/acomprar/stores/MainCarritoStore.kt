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

            if (added)
                Tools.Notifier.showToast(Literals.ToastText.ADDED_CARRITO_TO_MAIN_CARRITO)
            else
                Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_CARRITO_TO_MAIN_CARRITO)
        }
    }

    fun getAllCarrito() {
        viewModelScope.launch {
            val carritosAux: List<CarritoEntity> = withContext(Dispatchers.IO) {
                Database.getCarritosAddedToMainCarrito()
            }

            inmutableUpdateCarritos(carritosAux)
        }
    }

    fun deleteCarritoFromMainCarrito(id: Int, carritoStore: CarritoStore) {
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

    fun setUserIsBuying(value: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Database.setUserIsBuying(value)
            }

            var userBuyingAux: UserActionsEntity = _mainCarritoState.value.userBuying
            userBuyingAux.actionValue = value

            _mainCarritoState.value = _mainCarritoState.value.copy(
                userBuying = userBuyingAux
            )
            Tools.Notifier.showToast("Comienzas a comprar = $value")
        }
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

    fun loadCarritosToBuyList() {
        viewModelScope.launch {

        }
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
