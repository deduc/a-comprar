package org.ivandev.acomprar.database.special_classes

import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Producto

class CarritoComprarEntity {
    var carritos: List<Carrito> = listOf<Carrito>()
    var productos: List<Producto> = listOf<Producto>()
}