package org.ivandev.acomprar.database.entities

class Producto(
    val id: Int?,
    val idCategoria: Int?,
    val nombre: String,
    val cantidad: Float,
    val unidadCantidad: String,
    val Marca: String?,
)