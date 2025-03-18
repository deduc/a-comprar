package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.database.interfaces.ITools

class Menu(
    // Constructor
    id: Int? = null,
    val idProductos: List<Int>?,
    val nombre: String
) : ITools
{
    val id: Int

    init {
        this.id = id ?: makeId()
    }
}