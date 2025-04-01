package org.ivandev.acomprar.database.entities

data class Comida(
    val id: Int?,
    val idMenu: Int?,
    val nombre: String,
    // Lunes = 0, Martes = 1... domingo = 6
    val dia: Int,
    // comida = 0, cena = 1, postre = 2
    val tipo: Int,
)