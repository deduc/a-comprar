package org.ivandev.acomprar.database.entities

class MenuDaysOfWeekEntity (
    val id: Int,
    var idMenu: Int,
    val idComida: Int?,
    val idCena: Int?,
    val day: String,
)