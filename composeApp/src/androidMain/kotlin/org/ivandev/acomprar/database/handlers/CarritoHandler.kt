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

        return db.insert(
            Literals.Database.CARRITO_TABLE, null, values
        ) != -1L
    }

    fun addProductoToCurrentCarrito(db: SQLiteDatabase, carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        val carritoProductoFiltered: CarritoProductoEntity? = getCarritoProductoFiltered(db, carrito.id, producto.id)
        val newCantidad = if (carritoProductoFiltered != null) carritoProductoFiltered.cantidad + 1 else 1

        val row = ContentValues().apply {
            put(Literals.Database.ID_CARRITO_COLUMN, carrito.id)
            put(Literals.Database.ID_PRODUCTO_COLUMN, producto.id)
            put(Literals.Database.CANTIDAD_COLUMN, newCantidad)
        }

        return if (carritoProductoFiltered != null) {
            // Actualizar registro existente
            val whereClause = "${Literals.Database.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ID_PRODUCTO_COLUMN} = ?"
            val whereArgs = arrayOf(carrito.id.toString(), producto.id.toString())

            db.update(Literals.Database.CARRITO_PRODUCTO_TABLE, row, whereClause, whereArgs) > 0
        } else {
            // Insertar nuevo registro
            db.insert(Literals.Database.CARRITO_PRODUCTO_TABLE, null, row) != -1L
        }
    }

    fun substractProductoToCurrentCarrito(db: SQLiteDatabase, carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        val carritoProductoFiltered: CarritoProductoEntity? = getCarritoProductoFiltered(db, carrito.id, producto.id)
        val newCantidad = if (carritoProductoFiltered != null) carritoProductoFiltered.cantidad - 1 else 0

        if (newCantidad <= 0) return deleteProductFromCarritoById(db, carrito.id, producto.id)

        val row = ContentValues().apply {
            put(Literals.Database.ID_CARRITO_COLUMN, carrito.id)
            put(Literals.Database.ID_PRODUCTO_COLUMN, producto.id)
            put(Literals.Database.CANTIDAD_COLUMN, newCantidad)
        }

        return if (carritoProductoFiltered != null) {
            // Actualizar registro existente
            db.update(
                Literals.Database.CARRITO_PRODUCTO_TABLE,
                row,
                "${Literals.Database.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ID_PRODUCTO_COLUMN} = ?",
                arrayOf(carrito.id.toString(), producto.id.toString())
            ) > 0
        } else {
            // Insertar nuevo registro
            db.insert(Literals.Database.CARRITO_PRODUCTO_TABLE, null, row) != -1L
        }
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
        return db.query(
            Literals.Database.CARRITO_TABLE,
            null,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
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
        val carrito: CarritoEntity = getById(db, id)!!
        val idProductosByCarrito: List<Int> = getProductosIdByCarritoId(db, carrito)
        val productosList: MutableList<ProductoEntity> = getProductosByIds(db, idProductosByCarrito).toMutableList()

        val carritoProducto: List<CarritoProductoEntity> = getCarritoProductoListByCarritoId(db, id)

        // Mapeamos productos con su cantidad
        val productosAndCantidades: MutableList<Pair<ProductoEntity, Int>> = productosList.mapNotNull { producto ->
            val match = carritoProducto.find { it.idProducto == producto.id }
            match?.let { producto to it.cantidad }
        }.toMutableList()

        return CarritoAndProductsData(carrito, productosAndCantidades)
    }

    fun deleteProductFromCarritoById(db: SQLiteDatabase, idCarrito: Int, idProducto: Int): Boolean {
        return db.delete(
            Literals.Database.CARRITO_PRODUCTO_TABLE,
            "${Literals.Database.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ID_PRODUCTO_COLUMN} = ?",
            arrayOf(idCarrito.toString(), idProducto.toString())
        ) == 1
    }

    private fun getProductosByIds(db: SQLiteDatabase, idProductosByCarrito: List<Int>): List<ProductoEntity> {
        val productos = mutableListOf<ProductoEntity>()

        idProductosByCarrito.forEach {
            val producto: ProductoEntity? = ProductoHandler.getById(db, it)
            if (producto != null) productos.add(producto)
        }
        return productos
    }

    private fun getProductosIdByCarritoId(db: SQLiteDatabase, carrito: CarritoEntity): List<Int> {
        return db.rawQuery(
            "SELECT * FROM ${Literals.Database.CARRITO_PRODUCTO_TABLE} where ${Literals.Database.ID_CARRITO_COLUMN} == ${carrito.id}",
            null
        )
            .use {
                var idProductosList: MutableList<Int> = mutableListOf()

                if (it.moveToFirst()) {
                    do {
                        idProductosList.add(it.getInt(1))
                    } while (it.moveToNext())
                }

                idProductosList
            }
    }

    private fun getCarritoProductoFiltered(db: SQLiteDatabase, carritoId: Int, productoId: Int): CarritoProductoEntity? {
        return db.query(
            Literals.Database.CARRITO_PRODUCTO_TABLE,
            null,
            "${Literals.Database.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ID_PRODUCTO_COLUMN} = ?",
            arrayOf(carritoId.toString(), productoId.toString()),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                CarritoProductoEntity(
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_CARRITO_COLUMN)),
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ID_PRODUCTO_COLUMN)),
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.CANTIDAD_COLUMN)),
                )
            }
            else null
        }
    }

    private fun getCarritoProductoListByCarritoId(db: SQLiteDatabase, id: Int): List<CarritoProductoEntity> {
        val carritoProducto: List<CarritoProductoEntity> = db.query(
            Literals.Database.CARRITO_PRODUCTO_TABLE,
            null,
            "${Literals.Database.ID_CARRITO_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            val carritoProductoList = mutableListOf<CarritoProductoEntity>()

            if (it.moveToFirst()) {
                do {
                    carritoProductoList.add(
                        CarritoProductoEntity(
                            it.getInt(0), it.getInt(1), it.getInt(2)
                        )
                    )
                } while (it.moveToNext())
            }

            carritoProductoList
        }

        return carritoProducto
    }

    private fun getCarritoProductoContentValues(carritoProductoFiltered: CarritoProductoEntity?, carritoId: Int, productoId: Int): ContentValues {
        val row = ContentValues()
        val cantidad: Int

        if (carritoProductoFiltered != null) cantidad = carritoProductoFiltered.cantidad + 1
        else cantidad = 1

        row.put(Literals.Database.ID_CARRITO_COLUMN, carritoId)
        row.put(Literals.Database.ID_PRODUCTO_COLUMN, productoId)
        row.put(Literals.Database.CANTIDAD_COLUMN, cantidad)

        return row
    }
}