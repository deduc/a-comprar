package org.ivandev.acomprar.database.entities

class MainCarritoWithProductsEntity (
    val id: Int,
    val idProducto: Int,
    // cantidad de productos que hay que comprar
    val cantidad: Int,
    // falso booleano, 0 no comprados todos y 1 comprados todos
    val isComprado: Int
)
