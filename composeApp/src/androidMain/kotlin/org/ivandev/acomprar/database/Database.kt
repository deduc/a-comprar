package org.ivandev.acomprar.database

import android.content.Context
import org.ivandev.acomprar.database.entities.Carrito
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.interfaces.DatabaseMethods

object Database : DatabaseMethods {
    lateinit var mySQLiteDatabase: MySQLiteDatabase

    fun initializeDatabase(context: Context) {
        mySQLiteDatabase = MySQLiteDatabase(context)
    }




    override fun importJsonData() {}

    override fun getAllCarrito(): List<Carrito> {
        TODO("Not yet implemented")
    }

    override fun getAllCategoria(): List<Categoria> {
        return CategoriaHandler.getAll()
    }

    override fun getAllMenu(): List<Menu> {
        TODO("Not yet implemented")
    }

    override fun getAllProducto(): List<Producto> {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {}
    override fun deleteAllCarrito() {}
    override fun deleteAllCategoria() {}
    override fun deleteAllMenu() {}
    override fun deleteAllProducto() {}
}