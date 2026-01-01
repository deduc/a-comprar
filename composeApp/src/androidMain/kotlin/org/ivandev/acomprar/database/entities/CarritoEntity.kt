package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.Literals

class CarritoEntity(
    val id: Int,
    val name: String,
    val description: String
) {
    fun getFixedDescription(): String {
        if (description.isNullOrEmpty()) return Literals.SIN_DESCRIPCION_TEXT
        else return description
    }
    fun getFixedName(): String {
        if (name.isNullOrEmpty()) return Literals.SIN_NOMBRE_TEXT
        else return name
    }
}
