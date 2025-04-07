package org.ivandev.acomprar.database.entities

class CarritoEntity(
    val id: Int,
    val idProductos: List<Int>?,
    val description: String
)
