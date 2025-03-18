package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.database.interfaces.ITools

class Producto(
    // Constructor
    id: Int? = null,
    val idCategoria: Int?,
    val nombre: String,
    val cantidad: Float,
    val unidadCantidad: String,
    val Marca: String?,
) : ITools
{
    val id: Int

    init {
        this.id = id ?: makeId()
    }
}