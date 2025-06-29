package org.ivandev.acomprar.database.special_classes

import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CarritoProductoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity

class CarritoAndProductsData(
    val carrito: CarritoEntity,
    val productosAndCantidades: MutableList<Pair<ProductoEntity, Int>> = mutableListOf()
)
