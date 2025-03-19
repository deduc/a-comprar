package org.ivandev.acomprar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Carrito
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.handlers.CarritoHandler
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.MenuHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
import org.ivandev.acomprar.database.scripts.CreateTables
import org.ivandev.acomprar.database.scripts.DropTables

class MySQLiteDatabase(context: Context) : SQLiteOpenHelper(
    context, Literals.Database.DATABASE_NAME, null, 1
) {
    // Se ejecuta este método la primera vez que usas
    // las lineas db.writableDatabase o db.readableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            createTables(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            dropTables(it)

            createTables(it)
        }
    }


    fun insertIntoCarrito(carrito: Carrito) {
        writableDatabase.use { db ->
            CarritoHandler.insert(db, carrito)
        }
    }
    fun insertIntoCategoria(categoria: Categoria) {
        writableDatabase.use { db ->
            CategoriaHandler.insert(db, categoria)
        }
    }
    fun insertIntoMenu(menu: Menu) {
        writableDatabase.use { db ->
            MenuHandler.insert(db, menu)
        }
    }
    fun insertIntoProducto(producto: Producto) {
        writableDatabase.use { db ->
            ProductoHandler.insert(db, producto)
        }
    }


    private fun createTables(db: SQLiteDatabase) {
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO)
        db.execSQL(CreateTables.CREATE_TABLE_CATEGORIA)
        db.execSQL(CreateTables.CREATE_TABLE_MENU)
        db.execSQL(CreateTables.CREATE_TABLE_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO_PRODUCTO)
    }

    private fun dropTables(db: SQLiteDatabase){
        db.execSQL(DropTables.DROP_TABLE_CARRITO)
        db.execSQL(DropTables.DROP_TABLE_CATEGORIA)
        db.execSQL(DropTables.DROP_TABLE_MENU)
        db.execSQL(DropTables.DROP_TABLE_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_CARRITO_PRODUCTO)
    }
}
