package org.ivandev.acomprar.database.interfaces

import org.ivandev.acomprar.database.entities.Carrito
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.database.entities.Producto

interface DatabaseMethods {
    fun importJsonData()

    fun getAllCarrito(): List<Carrito>
    fun getAllCategoria(): List<Categoria>
    fun getAllMenu(): List<Menu>
    fun getAllProducto(): List<Producto>

    fun deleteAll()
    fun deleteAllCarrito()
    fun deleteAllCategoria()
    fun deleteAllMenu()
    fun deleteAllProducto()


}