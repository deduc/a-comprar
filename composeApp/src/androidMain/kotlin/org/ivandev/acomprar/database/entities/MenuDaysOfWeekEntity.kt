package org.ivandev.acomprar.database.entities

class MenuDaysOfWeekEntity (
    val id: Int,
    var idMenu: Int,
    // foreign keys a tabla Comida, el valpr no null indica si es comida o cena
    val idComida: Int?,
    val tipoComida: Int,
    // de luness a domingo
    val day: String,
)