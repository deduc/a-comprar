package org.ivandev.acomprar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.ComidaHandler
import org.ivandev.acomprar.database.handlers.MenuHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
import org.ivandev.acomprar.database.scripts.CreateTables
import org.ivandev.acomprar.database.scripts.DropTables
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.Producto

class MySQLiteDatabase(context: Context, version: Int) : SQLiteOpenHelper(
    context,
    Literals.Database.DATABASE_NAME,
    null,
    version
)
{
    // Se ejecuta este método la primera vez que usas
    // las lineas db.writableDatabase o db.readableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { it: SQLiteDatabase ->
            createTables(it)
            initializeData(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            dropTables(it)

            createTables(it)
        }
    }


    fun addCategoria(categoria: Categoria): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.insert(db, categoria)

        db.close()
        return result
    }

    fun addProducto(producto: Producto): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.insert(db, producto)

        db.close()
        return result
    }

    fun addMenuAndComidasYCenas(menu: Menu): Boolean {
        val db = writableDatabase
        var result = false

        try {
            db.beginTransaction() // Inicia una transacción para garantizar atomicidad

            val inserted = MenuHandler.insert(db, menu)
            if (!inserted) { return false }

            val lastMenu = MenuHandler.getLast(db) ?: return false
            result = ComidaHandler.insertComidasYCenasByMenuId(db, lastMenu.id!!)

            // Confirma la transacción si todo va bien
            if (result) { db.setTransactionSuccessful() }
        }
        catch (e: Exception) {
            e.printStackTrace() // Maneja excepciones para depuración
        }
        finally {
            db.endTransaction() // Finaliza la transacción (commit o rollback)
            db.close()
        }

        return result
    }


    fun getAllCategoria(): List<CategoriaEntity> {
        val db = readableDatabase
        val result = CategoriaHandler.getAll(db)

        db.close()
        return result
    }

    fun getAllProductosByCategoria(): List<CategoriaWithProductos> {
        val db = readableDatabase

        val categoriaEntities: List<CategoriaEntity> = CategoriaHandler.getAll(db)
        val result = ProductoHandler.getAllProductosByCategoria(db, categoriaEntities)

        db.close()
        return result
    }

    fun getAllMenu(): List<MenuEntity> {
        val db = readableDatabase
        val result = MenuHandler.getAll(db)

        db.close()
        return result
    }


    fun getProductosByCategoriaId(id: Int): List<ProductoEntity> {
        return ProductoHandler.getProductosByCategoriaId(readableDatabase, id)
    }

    fun getComidasByMenuId(id: Int): List<ComidaEntity> {
        val db = readableDatabase
        val result = ComidaHandler.getComidasByMenuId(db, id)

        db.close()
        return result
    }

    fun getLastMenu(): MenuEntity {
        val db = readableDatabase
        val result = MenuHandler.getLast(db)!!
        db.close()
        return result
    }



    fun updateCategoriaById(categoriaEntity: CategoriaEntity): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.updateCategoriaById(db, categoriaEntity)

        db.close()
        return result
    }

    fun updateProductoById(productoEntity: ProductoEntity): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.updateById(db, productoEntity)

        db.close()
        return result
    }

    fun updateProductosToSinCategoria(idCategoria: Int): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.updateProductosToSinCategoria(db, idCategoria)

        db.close()
        return result
    }



    fun deleteCategoriaById(id: Int): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.deleteById(db, id)
        db.close()
        return result
    }

    fun deleteProductoById(id: Int): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.deleteById(db, id)

        db.close()
        return result
    }


    fun deleteMenu(menuEntity: MenuEntity): Boolean {
        val db = writableDatabase
        val deletedMenu = MenuHandler.delete(db, menuEntity)
        var result = false

        if (deletedMenu) {
            result = ComidaHandler.deleteAllComidasByMenuId(db, menuEntity.id!!)
        }

        db.close()
        return result
    }

    // METODOS ESPECIALES
    // METODOS ESPECIALES
    // METODOS ESPECIALES
    private fun createTables(db: SQLiteDatabase) {
        println("Creando tablas")
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO)
        db.execSQL(CreateTables.CREATE_TABLE_CATEGORIA)
        db.execSQL(CreateTables.CREATE_TABLE_COMIDA)
        db.execSQL(CreateTables.CREATE_TABLE_COMIDA_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_MENU)
        db.execSQL(CreateTables.CREATE_TABLE_MENU_COMIDA)
        db.execSQL(CreateTables.CREATE_TABLE_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO_PRODUCTO)
    }

    private fun initializeData(db: SQLiteDatabase) {
        println("Inicializando base de datos")

        CategoriaHandler.initialize(db)
    }

    private fun dropTables(db: SQLiteDatabase){
        println("Borrando tablas")
        db.execSQL(DropTables.DROP_TABLE_CARRITO)
        db.execSQL(DropTables.DROP_TABLE_CATEGORIA)
        db.execSQL(DropTables.DROP_TABLE_COMIDA)
        db.execSQL(DropTables.DROP_TABLE_COMIDA_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_MENU)
        db.execSQL(DropTables.DROP_TABLE_MENU_COMIDA)
        db.execSQL(DropTables.DROP_TABLE_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_CARRITO_PRODUCTO)
    }
}
