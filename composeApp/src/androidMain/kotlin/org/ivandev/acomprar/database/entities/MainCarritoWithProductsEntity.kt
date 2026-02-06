package org.ivandev.acomprar.database.entities

/**
 * Almacenar el ID del maincarrito, los productos y cantidad que hay que comprar y su estado
 */
class MainCarritoWithProductsEntity (
    val id: Int,
    val idProducto: Int,
    // cantidad de productos que hay que comprar
    val cantidad: Int,
    // falso booleano, 0 no comprados todos y 1 comprados todos
    val isComprado: Int
)
