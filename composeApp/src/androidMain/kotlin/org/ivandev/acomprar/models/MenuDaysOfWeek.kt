package org.ivandev.acomprar.models

import androidx.compose.runtime.mutableStateOf

class MenuDaysOfWeek(
    id: Int? = null,
    idMenu: Int? = null,
    idComida: Int? = null,
    tipoComida: Int? = null,
    day: String? = null,
) {
    val id: Int? = id
    val idMenu: Int? = idMenu
    var idComida = mutableStateOf(idComida)
    var tipoComida: Int? = tipoComida
    val day: String? = day
}
