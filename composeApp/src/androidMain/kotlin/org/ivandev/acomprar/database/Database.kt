package org.ivandev.acomprar.database

import android.content.Context
import org.ivandev.acomprar.database.entities.Carrito
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Comida
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.special_classes.ProductosWithCategoria

object Database {
    lateinit var mySQLiteDatabase: MySQLiteDatabase
    private val dbVersion: Int = 5

    fun initializeDatabase(context: Context) {
        mySQLiteDatabase = MySQLiteDatabase(context, dbVersion)
    }

    fun importJsonData() {}
    fun deleteAll() {}
    fun deleteAllCarrito() {}
    fun deleteAllCategoria() {}
    fun deleteAllMenu() {}
    fun deleteAllProducto() {}


    fun addCategoria(categoria: Categoria): Boolean {
        return mySQLiteDatabase.addCategoria(categoria)
    }

    fun addProducto(producto: Producto): Boolean {
        return mySQLiteDatabase.addProducto(producto)
    }

    fun addMenu(menu: Menu): Boolean {
        return mySQLiteDatabase.addMenuAndComidasYCenas(menu)
    }

    fun getLastMenu(): Menu {
        return mySQLiteDatabase.getLastMenu()
    }



    fun getAllCarrito(): List<Carrito> {
        return listOf()
    }

    fun getAllCategoria(): List<Categoria> {
        return mySQLiteDatabase.getAllCategoria()
    }

    fun getAllMenu(): List<Menu> {
        return mySQLiteDatabase.getAllMenu()
    }

    fun getAllProducto(): List<Producto> {
        return listOf()
    }

    fun getAllProductosByCategoria(): List<ProductosWithCategoria> {
        return mySQLiteDatabase.getAllProductosByCategoria()
    }

    fun getProductosByCategoriaId(id: Int): List<Producto> {
        return mySQLiteDatabase.getProductosByCategoriaId(id)
    }

    fun getComidasByMenuId(id: Int): List<Comida> {
        return mySQLiteDatabase.getComidasByMenuId(id)
    }


    fun updateCategoriaById(categoria: Categoria): Boolean {
        return mySQLiteDatabase.updateCategoriaById(categoria)
    }

    fun updateProductoById(producto: Producto): Boolean {
        return mySQLiteDatabase.updateProductoById(producto)
    }


    fun deleteCategoriaById(idCategoria: Int): Boolean {
        var result = mySQLiteDatabase.deleteCategoriaById(idCategoria)

        if (result) {
            result = mySQLiteDatabase.updateProductosToSinCategoria(idCategoria)
        }

        return result
    }

    fun deleteProductoById(id: Int): Boolean {
        return mySQLiteDatabase.deleteProductoById(id)
    }

    fun deleteMenu(menu: Menu): Boolean {
        return mySQLiteDatabase.deleteMenu(menu)
    }
}