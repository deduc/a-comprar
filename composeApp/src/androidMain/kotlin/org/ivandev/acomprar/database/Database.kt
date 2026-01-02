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
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.models.Comida
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.models.Producto

object Database {
    lateinit var mySQLiteDatabase: MySQLiteDatabase
    private val dbVersion: Int = 23

    fun initializeDatabase(context: Context) {
        mySQLiteDatabase = MySQLiteDatabase(context, dbVersion)
    }

    fun restartDatabase() {
        mySQLiteDatabase.restartDatabase()
    }

    fun importJsonData() {}
    fun deleteAll() {}
    fun deleteAllCarrito() {}
    fun deleteAllCategoria() {}
    fun deleteAllMenu() {}
    fun deleteAllProducto(): Boolean {
        return mySQLiteDatabase.deleteAllProducto()
    }

    fun addCarrito(carrito: Carrito): Boolean {
        return mySQLiteDatabase.addCarrito(carrito)
    }

    fun addCategoria(categoriaEntity: Categoria): Boolean {
        return mySQLiteDatabase.addCategoria(categoriaEntity)
    }

    fun addCarritoToMainCarrito(id: Int): Boolean {
        return mySQLiteDatabase.addCarritoToMainCarrito(id)
    }

    fun addProducto(producto: Producto): Boolean {
        return mySQLiteDatabase.addProducto(producto)
    }

    fun addProductosList(productos: List<Producto>): Boolean {
        return mySQLiteDatabase.addProductosList(productos)
    }

    fun addProductoToCurrentCarrito(carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        return mySQLiteDatabase.addProductoToCurrentCarrito(carrito, producto)
    }

    fun substractProductoToCurrentCarrito(carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        return mySQLiteDatabase.substractProductoToCurrentCarrito(carrito, producto)
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

    fun checkIfCarritosWasAddedToMainCarrito(idCarritos: List<Int>): List<Int> {
        return mySQLiteDatabase.checkIfCarritosWasAddedToMainCarrito(idCarritos)
    }


    fun getCarritoAndProductosByCarritoId(id: Int): CarritoAndProductsData {
        return mySQLiteDatabase.getCarritoAndProductosByCarritoId(id)
    }

    fun getLastMenu(): MenuEntity? {
        return mySQLiteDatabase.getLastMenu()
    }

    fun getAllCarrito(): List<CarritoEntity> {
        return mySQLiteDatabase.getAllCarrito()
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

    fun getCategoriaIdByProductoId(producto: Producto): Int {
        return mySQLiteDatabase.getCategoriaIdByProductoId(producto)
    }


    fun getProductoById(id: Int): ProductoEntity? {
        return mySQLiteDatabase.getProductoById(id)
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

    fun updateCarrito(carrito: Carrito): Boolean {
        return mySQLiteDatabase.updateCarrito(carrito)
    }


    fun updateComidaById(comida: ComidaEntity): Boolean {
        return mySQLiteDatabase.updateComidaById(comida)
    }

    fun updateProductoById(productoEntity: ProductoEntity): Boolean {
        return mySQLiteDatabase.updateProductoById(productoEntity)
    }

    fun updateMenuNameById(menu: MenuEntity): Boolean {
        return mySQLiteDatabase.updateMenuNameById(menu)
    }

    fun updateMenuDaysOfWeekById(menuDaysOfWeek: MenuDaysOfWeek): Boolean {
        return mySQLiteDatabase.updateMenuDaysOfWeekById(menuDaysOfWeek)
    }



    fun deleteCategoriaById(idCategoria: Int): Boolean {
        var result = mySQLiteDatabase.deleteCategoriaById(idCategoria)

        if (result) {
            result = mySQLiteDatabase.updateProductosToSinCategoria(idCategoria)
        }

        return result
    }

    fun deleteCarritoById(id: Int): Boolean {
        return mySQLiteDatabase.deleteCarritoById(id)
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