package org.ivandev.acomprar.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.Producto

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


    fun addCategoria(categoriaEntity: Categoria): Boolean {
        return mySQLiteDatabase.addCategoria(categoriaEntity)
    }

    fun addProducto(producto: Producto): Boolean {
        return mySQLiteDatabase.addProducto(producto)
    }

    fun addMenu(menu: Menu): Boolean {
        return mySQLiteDatabase.addMenuAndComidasYCenas(menu)
    }

    fun getLastMenu(): MenuEntity {
        return mySQLiteDatabase.getLastMenu()
    }



    fun getAllCarrito(): List<CarritoEntity> {
        return listOf()
    }

    fun getAllCategoria(): List<CategoriaEntity> {
        return mySQLiteDatabase.getAllCategoria()
    }

    fun getAllMenu(): List<MenuEntity> {
        return mySQLiteDatabase.getAllMenu()
    }

    fun getAllProducto(): List<ProductoEntity> {
        return listOf()
    }

    fun getAllProductosByCategoria(): List<CategoriaWithProductos> {
        return mySQLiteDatabase.getAllProductosByCategoria()
    }

    fun getProductosByCategoriaId(id: Int): List<ProductoEntity> {
        return runBlocking {
            // Ejecuta el método en un hilo de fondo para evitar bloquear el hilo principal
            withContext(Dispatchers.IO) {
                mySQLiteDatabase.getProductosByCategoriaId(id)
            }
        }
    }

    fun getComidasByMenuId(id: Int): List<ComidaEntity> {
        return mySQLiteDatabase.getComidasByMenuId(id)
    }


    fun updateCategoriaById(categoriaEntity: CategoriaEntity): Boolean {
        return mySQLiteDatabase.updateCategoriaById(categoriaEntity)
    }

    fun updateProductoById(productoEntity: ProductoEntity): Boolean {
        return mySQLiteDatabase.updateProductoById(productoEntity)
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

    fun deleteMenu(menuEntity: MenuEntity): Boolean {
        return mySQLiteDatabase.deleteMenu(menuEntity)
    }
}