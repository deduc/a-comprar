package org.ivandev.acomprar.models

data class Comida(
    var id: Int,
    var idMenu: Int,
    var nombre: String,
    // Lunes = 0, Martes = 1... domingo = 6
    var dia: Int,
    // comida = 0, cena = 1, postre = 2
    var tipo: Int,
)