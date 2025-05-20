package org.ivandev.acomprar.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MenuDaysOfWeek(
    id: Int? = null,
    idMenu: Int? = null,
    idComida: Int? = null,
    tipoComida: Int? = null,
    day: String? = null,
) {
    val id: Int? = id
    val idMenu: Int? = idMenu
    var idComida by mutableStateOf<Int?>(idComida)
    var tipoComida: Int? = tipoComida
    val day: String? = day
}
