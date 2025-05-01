package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.Literals

class ProductoEntity(
    val id: Int,
    val idCategoria: Int,
    val nombre: String,
    val cantidad: String?,
    val marca: String?,
) {
    fun getCantidadFixed(): String {
        if (cantidad == "") {
            return Literals.SIN_CANTIDAD_TEXT
        }
        else {
            return cantidad!!
        }
    }

    fun getMarcaFixed(): String {
        if (marca == "") {
            return Literals.SIN_MARCA_TEXT
        }
        else {
            return marca!!
        }
    }

    fun getCantidadOrVoid(): String {
        if (cantidad == "") {
            return cantidad
        }
        else {
            return cantidad!!
        }
    }

    fun getMarcaOrVoid(): String {
        if (marca == "") {
            return marca
        }
        else {
            return marca!!
        }
    }
}