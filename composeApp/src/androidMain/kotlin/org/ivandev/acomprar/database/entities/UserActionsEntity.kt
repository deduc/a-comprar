package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum

class UserActionsEntity(
    var id: Int = 0,
    var actionType: String = "",
    var actionValue: String = "",
    var timestamp: String = ""
) {
    fun userIsBuying(): Boolean {
        return actionType == Literals.UserActions.USER_BUYING && actionValue == UserBuyingEnum.USER_IS_BUYING
    }
}