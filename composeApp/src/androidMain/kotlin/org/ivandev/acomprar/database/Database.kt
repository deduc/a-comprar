package org.ivandev.acomprar.database

import android.content.Context
import org.ivandev.acomprar.database.entities.Carrito
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.entities.ProductosWithCategoria

object Database {
    lateinit var mySQLiteDatabase: MySQLiteDatabase

    fun initializeDatabase(context: Context) {
        mySQLiteDatabase = MySQLiteDatabase(context)
    }
    fun importJsonData() {}
    fun deleteAll() {}
    fun deleteAllCarrito() {}
    fun deleteAllCategoria() {}
    fun deleteAllMenu() {}
    fun deleteAllProducto() {}


    fun getAllCarrito(): List<Carrito> {
        TODO("Not yet implemented")
    }

    fun getAllCategoria(): List<Categoria> {
        return mySQLiteDatabase.getAllCategoria()
    }

    fun getAllMenu(): List<Menu> {
        TODO("Not yet implemented")
    }

    fun getAllProducto(): List<Producto> {
        TODO("Not yet implemented")
    }

    fun getProductosByCategoriaId(id: Int): List<Producto> {
        return mySQLiteDatabase.getProductosByCategoriaId(id)
    }

    fun getAllProductosByCategoria(): List<ProductosWithCategoria> {
        return mySQLiteDatabase.getAllProductosByCategoria()
    }

    fun addCategoria(categoria: Categoria): Boolean {
        return mySQLiteDatabase.addCategoria(categoria)
    }

    fun addProducto(producto: Producto): Boolean {
        return mySQLiteDatabase.addProducto(producto)
    }

    fun deleteCategoriaById(id: Int) {
        mySQLiteDatabase.deleteCategoriaById(id)
    }
}