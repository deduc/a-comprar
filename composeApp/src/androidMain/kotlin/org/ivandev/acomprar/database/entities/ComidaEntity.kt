package org.ivandev.acomprar.database.entities

data class ComidaEntity(
    var id: Int,
    var nombre: String,
    // comida = 0, cena = 1, postre = 2
    var tipo: Int,
)