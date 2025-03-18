package org.ivandev.acomprar.database

import org.ivandev.acomprar.database.handlers.CarritoHandler
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.MenuHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
import org.ivandev.acomprar.database.interfaces.DatabaseMethods

object Database : DatabaseMethods {
    var carritoHandler = CarritoHandler
    var categoriaHandler = CategoriaHandler
    var menuHandler = MenuHandler
    var productoHandler = ProductoHandler

    override fun importJsonData() {}
    override fun deleteAll() {}
    override fun deleteAllCarrito() {}
    override fun deleteAllCategoria() {}
    override fun deleteAllMenu() {}
    override fun deleteAllProducto() {}
}