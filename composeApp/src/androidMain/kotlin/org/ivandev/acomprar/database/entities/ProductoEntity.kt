package org.ivandev.acomprar.database.entities

class ProductoEntity(
    val id: Int,
    val idCategoria: Int,
    val nombre: String,
    val cantidad: String?,
    val marca: String?,
)