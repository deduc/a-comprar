package org.ivandev.acomprar.models

data class Comida(
    var id: Int?,
    var nombre: String,
    // comida = 0, cena = 1, postre = 2
    var tipo: Int,
)