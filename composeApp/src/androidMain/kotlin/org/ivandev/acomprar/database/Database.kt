package org.ivandev.acomprar.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.models.Comida
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.models.Producto

object Database {
    lateinit var mySQLiteDatabase: MySQLiteDatabase
    private val dbVersion: Int = 13

    fun initializeDatabase(context: Context) {
        mySQLiteDatabase = MySQLiteDatabase(context, dbVersion)
    }

    fun importJsonData() {}
    fun deleteAll() {}
    fun deleteAllCarrito() {}
    fun deleteAllCategoria() {}
    fun deleteAllMenu() {}
    fun deleteAllProducto(): Boolean {
        return mySQLiteDatabase.deleteAllProducto()
    }


    fun addCategoria(categoriaEntity: Categoria): Boolean {
        return mySQLiteDatabase.addCategoria(categoriaEntity)
    }

    fun addProducto(producto: Producto): Boolean {
        return mySQLiteDatabase.addProducto(producto)
    }

    fun addProductosList(productos: List<Producto>): Boolean {
        return mySQLiteDatabase.addProductosList(productos)
    }

    fun addMenu(menu: Menu): Boolean {
        return mySQLiteDatabase.addMenuAndComidasYCenas(menu)
    }

    fun addMenuDays(menuId: Int, menuDays: List<MenuDaysOfWeek>): Boolean {
        return mySQLiteDatabase.addMenuDays(menuId, menuDays)
    }

    fun addComida(comida: Comida): ComidaEntity? {
        return mySQLiteDatabase.addComida(comida)
    }


    fun getLastMenu(): MenuEntity? {
        return mySQLiteDatabase.getLastMenu()
    }

    fun getAllCarrito(): List<CarritoEntity> {
        return listOf()
    }

    fun getAllComidas(): MutableList<ComidaEntity> {
        return mySQLiteDatabase.getAllComidas()
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

    fun getComidasByTipoId(id: Int): List<ComidaEntity> {
        return mySQLiteDatabase.getComidasByTipoId(id)
    }

    fun getMenuDaysOfWeekByMenuId(menuId: Int): MutableList<MenuDaysOfWeekEntity> {
        return mySQLiteDatabase.getMenuDaysOfWeekByMenuId(menuId)
    }


    fun updateCategoriaById(categoriaEntity: CategoriaEntity): Boolean {
        return mySQLiteDatabase.updateCategoriaById(categoriaEntity)
    }

    fun updateProductoById(productoEntity: ProductoEntity): Boolean {
        return mySQLiteDatabase.updateProductoById(productoEntity)
    }

    fun updateMenuNameById(menu: MenuEntity): Boolean {
        return mySQLiteDatabase.updateMenuNameById(menu)
    }



    fun deleteCategoriaById(idCategoria: Int): Boolean {
        var result = mySQLiteDatabase.deleteCategoriaById(idCategoria)

        if (result) {
            result = mySQLiteDatabase.updateProductosToSinCategoria(idCategoria)
        }

        return result
    }

    fun deleteComidaById(comidaId: Int): Boolean {
        return mySQLiteDatabase.deleteComidaById(comidaId)
    }

    fun deleteProductoById(id: Int): Boolean {
        return mySQLiteDatabase.deleteProductoById(id)
    }

    fun deleteMenu(menuEntity: MenuEntity): Boolean {
        return mySQLiteDatabase.deleteMenu(menuEntity)
    }

    fun deleteLastMenu(): Boolean {
        return mySQLiteDatabase.deleteLastMenu()
    }
}