package org.ivandev.acomprar.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.Database.mySQLiteDatabase
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.entities.ProductosWithCategoria
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
import org.ivandev.acomprar.database.scripts.CreateTables
import org.ivandev.acomprar.database.scripts.DropTables

class MySQLiteDatabase(context: Context) : SQLiteOpenHelper(
    context, Literals.Database.DATABASE_NAME, null, 1
) {
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



    fun getAllCategoria(): List<Categoria> {
        val db = readableDatabase
        val result = CategoriaHandler.getAll(db)

        db.close()
        return result
    }

    fun getAllProductosByCategoria(): List<ProductosWithCategoria> {
        val db = readableDatabase

        val categorias: List<Categoria> = CategoriaHandler.getAll(db)
        val result = ProductoHandler.getAllProductosByCategoria(db, categorias)

        db.close()
        return result
    }

    fun getProductosByCategoriaId(id: Int): List<Producto> {
        val db = readableDatabase
        val result = ProductoHandler.getProductosByCategoriaId(db, id)

        db.close()
        return result
    }


    fun updateCategoriaById(categoria: Categoria): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.updateCategoriaById(db, categoria)

        db.close()
        return result
    }

    fun updateProductoById(producto: Producto): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.updateById(db, producto)

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


    private fun createTables(db: SQLiteDatabase) {
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO)
        db.execSQL(CreateTables.CREATE_TABLE_CATEGORIA)
        db.execSQL(CreateTables.CREATE_TABLE_MENU)
        db.execSQL(CreateTables.CREATE_TABLE_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO_PRODUCTO)
    }

    private fun initializeData(db: SQLiteDatabase) {
        val categoria = ContentValues().apply {
            put(Literals.Database.ID_COLUMN, 0)
            put(Literals.Database.NOMBRE_COLUMN, Literals.NO_CATEGORY_TEXT)
        }

        db.insert(
            Literals.Database.CATEGORIA_TABLE,
            null,
            categoria
        )
    }

    private fun dropTables(db: SQLiteDatabase){
        db.execSQL(DropTables.DROP_TABLE_CARRITO)
        db.execSQL(DropTables.DROP_TABLE_CATEGORIA)
        db.execSQL(DropTables.DROP_TABLE_MENU)
        db.execSQL(DropTables.DROP_TABLE_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_CARRITO_PRODUCTO)
    }
}
