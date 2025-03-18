package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.database.interfaces.ITools

class Categoria(
    // Constructor
    id: Int? = null,
    val nombre: String,
) : ITools
{
    val id: Int

    init {
        this.id = id ?: makeId()
    }
}