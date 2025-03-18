package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.database.interfaces.ITools

class Carrito(
    // Constructor
    id: Int? = null,
    val idProductos: List<Int>?,
    val description: String
) : ITools
{
    val id: Int

    init {
        this.id = id ?: makeId()
    }
}