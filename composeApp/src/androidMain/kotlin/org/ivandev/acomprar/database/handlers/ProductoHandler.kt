package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Producto

object ProductoHandler {
    fun insert(db: SQLiteDatabase, producto: Producto): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ID_CATEGORIA_COLUMN, producto.idCategoria)
            put(Literals.Database.NOMBRE_COLUMN, producto.nombre)
            put(Literals.Database.CANTIDAD_COLUMN, producto.cantidad)
            put(Literals.Database.MARCA_COLUMN, producto.marca)
        }

        val rowId = db.insert(Literals.Database.PRODUCTO_TABLE, null, values)

        if (rowId == -1L)
            return false
        else
            return true
    }

    fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        val deletedRows: Int = db.delete(
            Literals.Database.PRODUCTO_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${id}")
        )

        return deletedRows == 1
    }

    fun getAll(db: SQLiteDatabase): MutableList<ProductoEntity> {
        val result: MutableList<ProductoEntity> = mutableListOf()

        val cursor = db.query(
            Literals.Database.PRODUCTO_TABLE, null,null,null,null,null,null
        )

        cursor.use {
            while (it.moveToNext()) {
                val productoEntity = ProductoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    idCategoria = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_CATEGORIA_COLUMN)),
                    nombre = it.getString(it.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                    cantidad = it.getString(it.getColumnIndexOrThrow(Literals.Database.CANTIDAD_COLUMN)),
                    marca = it.getString(it.getColumnIndexOrThrow(Literals.Database.MARCA_COLUMN))
                )

                result.add(productoEntity)
            }
        }

        return result
    }

    fun getById(db: SQLiteDatabase, id: Int): ProductoEntity? {
        var producto: ProductoEntity? = null

        val queryResult = db.query(
            Literals.Database.PRODUCTO_TABLE,
            null,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        queryResult.use {
            if (it.moveToFirst()) {
                producto = ProductoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    idCategoria = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_CATEGORIA_COLUMN)),
                    nombre = it.getString(it.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                    cantidad = it.getString(it.getColumnIndexOrThrow(Literals.Database.CANTIDAD_COLUMN)),
                    marca = it.getString(it.getColumnIndexOrThrow(Literals.Database.MARCA_COLUMN))
                )
            }
        }

        return producto
    }

    fun getProductosByCategoriaId(db: SQLiteDatabase, id: Int): List<ProductoEntity> {
        val productoEntities = mutableListOf<ProductoEntity>()

        val cursor = db.query(
            Literals.Database.PRODUCTO_TABLE, // Nombre de la tabla
            null, // Obtiene todas las columnas
            "${Literals.Database.ID_CATEGORIA_COLUMN} = ?", // Condición WHERE
            arrayOf(id.toString()), // Valores para reemplazar el "?"
            null,
            null,
            null
        )

        // Usamos el cursor con 'use' para que se cierre automáticamente cuando ya no se necesita
        cursor.use {
            while (it.moveToNext()) {
                val productoEntity = ProductoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    idCategoria = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_CATEGORIA_COLUMN)),
                    nombre = it.getString(it.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                    cantidad = it.getString(it.getColumnIndexOrThrow(Literals.Database.CANTIDAD_COLUMN)),
                    marca = it.getString(it.getColumnIndexOrThrow(Literals.Database.MARCA_COLUMN))
                )
                productoEntities.add(productoEntity)
            }
        }

        return productoEntities
    }

    fun getAllProductosByCategoria(db: SQLiteDatabase, categoriaEntities: List<CategoriaEntity>): List<CategoriaWithProductos> {
        val productsByCategoria: MutableList<CategoriaWithProductos> = mutableListOf()

        categoriaEntities?.forEach { categoriaEntity: CategoriaEntity ->
            var productoEntities: List<ProductoEntity>? = getProductosByCategoriaId(db, categoriaEntity.id!!)

            productsByCategoria.add(
                CategoriaWithProductos(
                    categoriaName = categoriaEntity.nombre,
                    categoriaId = categoriaEntity.id!!,
                    productoEntities
                )
            )
        }

        return productsByCategoria
    }

    fun updateById(db: SQLiteDatabase, productoEntity: ProductoEntity): Boolean {
        var productKeyValueColumn = ContentValues()
        productKeyValueColumn.put(Literals.Database.ID_COLUMN, productoEntity.id)
        productKeyValueColumn.put(Literals.Database.ID_CATEGORIA_COLUMN, productoEntity.idCategoria)
        productKeyValueColumn.put(Literals.Database.NOMBRE_COLUMN, productoEntity.nombre)
        productKeyValueColumn.put(Literals.Database.CANTIDAD_COLUMN, productoEntity.cantidad)
        productKeyValueColumn.put(Literals.Database.MARCA_COLUMN, productoEntity.marca)

        var productosUpdated: Int = db.update(
            Literals.Database.PRODUCTO_TABLE,
            productKeyValueColumn,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${productoEntity.id}")
        )

        return productosUpdated == 1
    }

    fun updateProductosToSinCategoria(db: SQLiteDatabase, idCategoria: Int): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ID_CATEGORIA_COLUMN, Literals.Database.ID_SIN_CATEGORIA_VALUE)
        }

        val rowsAffected = db.update(
            Literals.Database.PRODUCTO_TABLE,
            values,
            "${Literals.Database.ID_CATEGORIA_COLUMN} = ?",
            arrayOf(idCategoria.toString()))

        return rowsAffected > 0
    }
}