package org.ivandev.acomprar.stores.special_classes

import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos

data class MainCarritoState(
    val carritos: List<CarritoEntity> = emptyList(),
    val showAComprarPopup: Boolean = false,
    val showStopBuyingPopup: Boolean = false,
    val userBuying: UserActionsEntity = UserActionsEntity(),
    val carritosToBuy: List<CategoriaWithProductos> = emptyList(),
    val stoppedBuying: Boolean = false,
    val idCarritosAddedToMainCarrito: List<Int> = emptyList()
)
