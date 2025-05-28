package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CarritoProductoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.models.Carrito

object CarritoHandler {
    fun add(db: SQLiteDatabase, carrito: Carrito): Boolean {
        val values = ContentValues()

        values.put(Literals.Database.ID_COLUMN, carrito.id)
        values.put(Literals.Database.NOMBRE_COLUMN, carrito.name)
        values.put(Literals.Database.DESCRIPTION_COLUMN, carrito.description)

        val inserted: Long = db.insert(
            Literals.Database.CARRITO_TABLE,
            null,
            values
        )

        return inserted != -1L
    }

    fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        return db.delete(
            Literals.Database.CARRITO_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(id.toString())
        ) == 1
    }


    fun getAll(db: SQLiteDatabase): List<CarritoEntity> {
        val carritos: MutableList<CarritoEntity> = mutableListOf()

        db.query(Literals.Database.CARRITO_TABLE, null, null, null, null, null, null)
            .use { it: Cursor ->
                if (it.moveToFirst()) {
                    do {
                        carritos.add(
                            CarritoEntity(
                                it.getInt(0),
                                it.getString(1),
                                it.getString(2)
                            )
                        )
                    } while (it.moveToNext())
                }
            }

        return carritos.toList()
    }

    fun getById(db: SQLiteDatabase, id: Int): CarritoEntity? {
        val resultQuery = db.query(
            Literals.Database.CARRITO_TABLE,
            null,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return resultQuery.use {
            if (it.moveToFirst()) {
                CarritoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    name = it.getString(it.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                    description = it.getString(it.getColumnIndexOrThrow(Literals.Database.DESCRIPTION_COLUMN))
                )
            }
            else null
        }
    }

    fun getCarritoAndProductosByCarritoId(db: SQLiteDatabase, id: Int): CarritoAndProductsData {
        val carritoProducto: CarritoProductoEntity
        val carrito: CarritoEntity?
        val productosList: List<ProductoEntity>

        carrito = getById(db, id)

        if (carrito != null) {
            val idProductosByCarrito: List<Int> = getProductosIdByCarritoId(db, carrito)
            productosList = getProductosByIds(db, idProductosByCarrito)
        }

        return CarritoAndProductsData(
            carrito!!,
            mutableListOf()
        )
    }

    private fun getProductosByIds(db: SQLiteDatabase, idProductosByCarrito: List<Int>): List<ProductoEntity> {
        val result = mutableListOf<ProductoEntity>()

        idProductosByCarrito.forEach {
            val producto: ProductoEntity? = ProductoHandler.getById(db, it)
            if (producto != null) result.add(producto)
        }

        return result
    }

    private fun getProductosIdByCarritoId(db: SQLiteDatabase, carrito: CarritoEntity): List<Int> {
        val getAllProductosIdQuery = "SELECT * FROM ${Literals.Database.CARRITO_PRODUCTO_TABLE} where ${Literals.Database.ID_CARRITO_COLUMN} == ${carrito.id}"

        val idProductos = db.rawQuery(getAllProductosIdQuery, null)
            .use {
                var idProductosList: MutableList<Int> = mutableListOf()

                if (it.moveToFirst()) {
                    do {
                        idProductosList.add(it.getInt(1))
                    } while (it.moveToNext())
                }

                idProductosList
            }

        return idProductos
    }
}