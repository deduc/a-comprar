package org.ivandev.acomprar.stores.special_classes

import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity

data class MainCarritoState(
    val carritos: List<CarritoEntity> = emptyList(),
    val showAComprarPopup: Boolean = false,
    val showStopBuyingPopup: Boolean = false,
    val userBuying: UserActionsEntity = UserActionsEntity(),
)
