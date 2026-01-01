package org.ivandev.acomprar.database.special_classes

import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity

class CarritoAndProductsData(
    var carrito: CarritoEntity,
    var productosAndCantidades: MutableList<Pair<ProductoEntity, Int>> = mutableListOf()
)
