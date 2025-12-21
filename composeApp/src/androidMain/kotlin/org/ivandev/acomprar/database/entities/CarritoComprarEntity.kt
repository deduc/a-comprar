package org.ivandev.acomprar.database.entities

import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Producto

class CarritoComprarEntity {
    val carritos: List<Carrito> = listOf<Carrito>()
    val productos: List<Producto> = listOf<Producto>()
}
