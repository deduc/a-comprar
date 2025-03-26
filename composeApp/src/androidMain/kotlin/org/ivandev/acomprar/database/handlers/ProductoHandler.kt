package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.entities.ProductosWithCategoria
import org.ivandev.acomprar.database.interfaces.DatabaseCRUD

object ProductoHandler: DatabaseCRUD<Producto> {
    override fun insert(db: SQLiteDatabase, producto: Producto): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ID_CATEGORIA_COLUMN, producto.idCategoria)
            put(Literals.Database.NOMBRE_COLUMN, producto.nombre)
            put(Literals.Database.CANTIDAD_COLUMN, producto.cantidad)
            put(Literals.Database.UNIDAD_CANTIDAD_COLUMN, producto.unidadCantidad)
            put(Literals.Database.MARCA_COLUMN, producto.marca)
        }

        val rowId = db.insert(Literals.Database.PRODUCTO_TABLE, null, values)

        if (rowId == -1L)
            return false
        else
            return true
    }

    fun getProductosByCategoriaId(db: SQLiteDatabase, id: Int): List<Producto> {
        val productos = mutableListOf<Producto>()

        val cursor = db.query(
            Literals.Database.PRODUCTO_TABLE, // Nombre de la tabla
            null, // Obtiene todas las columnas
            "${Literals.Database.ID_CATEGORIA_COLUMN} = ?", // Condición WHERE
            arrayOf(id.toString()), // Valores para reemplazar el "?"
            null,
            null,
            null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val producto = Producto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ID_CATEGORIA_COLUMN)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                    cantidad = cursor.getFloat(cursor.getColumnIndexOrThrow(Literals.Database.CANTIDAD_COLUMN)),
                    unidadCantidad = cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.UNIDAD_CANTIDAD_COLUMN)),
                    marca = cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.MARCA_COLUMN))
                )
                productos.add(producto)
            }
        }

        return productos
    }

    fun getAllProductosByCategoria(
        db: SQLiteDatabase,
        categorias: List<Categoria>
    ): List<ProductosWithCategoria> {
        val productsByCategoria: MutableList<ProductosWithCategoria> = mutableListOf()

        categorias?.forEach { categoria: Categoria ->
            var productos: List<Producto>? = getProductosByCategoriaId(db, categoria.id!!)

            productsByCategoria.add(
                ProductosWithCategoria(
                    categoriaName = categoria.nombre,
                    categoriaId = categoria.id,
                    productos
                )
            )
        }

        return productsByCategoria
    }

    override fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        val deletedRows: Int = db.delete(
            Literals.Database.PRODUCTO_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${id}")
        )

        return deletedRows == 1
    }

    override fun getAll(db: SQLiteDatabase): MutableList<Producto> {
        TODO("Not yet implemented")
    }
}