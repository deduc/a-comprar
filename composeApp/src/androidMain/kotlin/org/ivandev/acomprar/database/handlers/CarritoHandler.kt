package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CarritoProductoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.models.Carrito

object CarritoHandler {
    fun initialize(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.ID_COLUMN, Literals.Database.HardcodedValues.MAIN_CARRITO_ID)
            put(Literals.Database.ColumnNames.NOMBRE_COLUMN, Literals.Database.HardcodedValues.CARRITO_BASTARDO_NAME)
            put(Literals.Database.ColumnNames.DESCRIPTION_COLUMN, Literals.Database.HardcodedValues.CARRITO_BASTARDO_DESCRIPTION)
        }
        val inserted: Long = db.insert(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            values,
        )

        if (inserted.toInt() == 1)
            println("[DEBUG] Carrito bastardo añadido con éxito")
        else
            println("ERROR AÑADIENDO CARRITO BASTARDO")
    }

    fun add(db: SQLiteDatabase, carrito: Carrito): Boolean {
        val values = ContentValues()

        values.put(Literals.Database.ColumnNames.ID_COLUMN, carrito.id)
        values.put(Literals.Database.ColumnNames.NOMBRE_COLUMN, carrito.name)
        values.put(Literals.Database.ColumnNames.DESCRIPTION_COLUMN, carrito.description)

        return db.insert(
            Literals.Database.Tables.CARRITO_TABLE, null, values
        ) != -1L
    }

    fun addProductoToCurrentCarrito(db: SQLiteDatabase, carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        val carritoProductoFiltered: CarritoProductoEntity? = getCarritoProductoFiltered(db, carrito.id, producto.id)
        val newCantidad = if (carritoProductoFiltered != null) carritoProductoFiltered.cantidad + 1 else 1

        val row = ContentValues().apply {
            put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, carrito.id)
            put(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN, producto.id)
            put(Literals.Database.ColumnNames.CANTIDAD_COLUMN, newCantidad)
        }

        return if (carritoProductoFiltered != null) {
            // Actualizar registro existente
            val whereClause = "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN} = ?"
            val whereArgs = arrayOf(carrito.id.toString(), producto.id.toString())

            db.update(Literals.Database.Tables.CARRITO_PRODUCTO_TABLE, row, whereClause, whereArgs) > 0
        } else {
            // Insertar nuevo registro
            db.insert(Literals.Database.Tables.CARRITO_PRODUCTO_TABLE, null, row) != -1L
        }
    }

    fun addLotProductos(db: SQLiteDatabase, carrito: CarritoEntity, items: List<ProductoEntity>): Boolean {
        if (items.isEmpty()) return true

        return try {
            db.beginTransaction()

            items.forEach { producto ->
                val values = ContentValues().apply {
                    put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, carrito.id)
                    put(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN, producto.id)
                    put(Literals.Database.ColumnNames.CANTIDAD_COLUMN, producto.cantidad)
                }

                val rowId = db.insert(
                    Literals.Database.Tables.CARRITO_PRODUCTO_TABLE,
                    null,
                    values
                )

                if (rowId == -1L) throw SQLException("Error insertando producto ${producto.id}")
            }

            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
        }
    }

    fun checkIfCarritoExists(db: SQLiteDatabase, carrito: Carrito): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.NOMBRE_COLUMN, carrito.name)
            put(Literals.Database.ColumnNames.DESCRIPTION_COLUMN, carrito.description)
        }

        var result = false

        db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            "${Literals.Database.ColumnNames.NOMBRE_COLUMN} = ? AND ${Literals.Database.ColumnNames.DESCRIPTION_COLUMN} = ?",
            arrayOf(carrito.name, carrito.description),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) result = true
            else result = false
        }

        return result
    }


    fun substractProductoToCurrentCarrito(db: SQLiteDatabase, carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        val carritoProductoFiltered: CarritoProductoEntity? = getCarritoProductoFiltered(db, carrito.id, producto.id)
        val newCantidad = if (carritoProductoFiltered != null) carritoProductoFiltered.cantidad - 1 else 0

        if (newCantidad <= 0) return deleteProductFromCarritoById(db, carrito.id, producto.id)

        val row = ContentValues().apply {
            put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, carrito.id)
            put(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN, producto.id)
            put(Literals.Database.ColumnNames.CANTIDAD_COLUMN, newCantidad)
        }

        return if (carritoProductoFiltered != null) {
            // Actualizar registro existente
            db.update(
                Literals.Database.Tables.CARRITO_PRODUCTO_TABLE,
                row,
                "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN} = ?",
                arrayOf(carrito.id.toString(), producto.id.toString())
            ) > 0
        } else {
            // Insertar nuevo registro
            db.insert(Literals.Database.Tables.CARRITO_PRODUCTO_TABLE, null, row) != -1L
        }
    }


    fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        return db.delete(
            Literals.Database.Tables.CARRITO_TABLE,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(id.toString())
        ) == 1
    }


    fun getAll(db: SQLiteDatabase): List<CarritoEntity> {
        val carritos: MutableList<CarritoEntity> = mutableListOf()

        db.query(Literals.Database.Tables.CARRITO_TABLE, null, null, null, null, null, null)
            .use { it: Cursor ->
                while (it.moveToNext()) {
                    carritos.add(
                        CarritoEntity(
                            it.getInt(0),
                            it.getString(1),
                            it.getString(2)
                        )
                    )
                }
            }

        return carritos.toList()
    }

    fun getCarritoByNameDescription(db: SQLiteDatabase, name: String, description: String): CarritoEntity? {
        var carrito: CarritoEntity? = null

        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.NOMBRE_COLUMN, name)
            put(Literals.Database.ColumnNames.DESCRIPTION_COLUMN, description)
        }

        db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            "${Literals.Database.ColumnNames.NOMBRE_COLUMN} = ? AND ${Literals.Database.ColumnNames.DESCRIPTION_COLUMN} = ?",
            arrayOf(name, description),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                carrito = CarritoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                    name = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                    description = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.DESCRIPTION_COLUMN))
                )
            }
        }

        return carrito
    }

    fun getById(db: SQLiteDatabase, id: Int): CarritoEntity? {
        return db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                CarritoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                    name = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                    description = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.DESCRIPTION_COLUMN))
                )
            }
            else null
        }
    }

    fun getCarritoById(db: SQLiteDatabase, id: Int): CarritoEntity {
        val result: CarritoEntity

        db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                result = CarritoEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                    name = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                    description = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.DESCRIPTION_COLUMN)),
                )
            }
            else {
                result = CarritoEntity(
                    id = 0,
                    name = "",
                    description = ""
                )
            }
        }

        return result
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
            Literals.Database.Tables.CARRITO_PRODUCTO_TABLE,
            "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN} = ?",
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
            "SELECT * FROM ${Literals.Database.Tables.CARRITO_PRODUCTO_TABLE} where ${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} == ${carrito.id}",
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
            Literals.Database.Tables.CARRITO_PRODUCTO_TABLE,
            null,
            "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ? AND ${Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN} = ?",
            arrayOf(carritoId.toString(), productoId.toString()),
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                CarritoProductoEntity(
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_CARRITO_COLUMN)),
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN)),
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.CANTIDAD_COLUMN)),
                )
            }
            else null
        }
    }

    private fun getCarritoProductoListByCarritoId(db: SQLiteDatabase, id: Int): List<CarritoProductoEntity> {
        val carritoProducto: List<CarritoProductoEntity> = db.query(
            Literals.Database.Tables.CARRITO_PRODUCTO_TABLE,
            null,
            "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ?",
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

        row.put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, carritoId)
        row.put(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN, productoId)
        row.put(Literals.Database.ColumnNames.CANTIDAD_COLUMN, cantidad)

        return row
    }

    fun updateCarrito(db: SQLiteDatabase, newCarrito: Carrito): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.NOMBRE_COLUMN, newCarrito.name)
            put(Literals.Database.ColumnNames.DESCRIPTION_COLUMN, newCarrito.description)
        }

        return db.update(
            Literals.Database.Tables.CARRITO_TABLE,
            values,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(newCarrito.id.toString())
        ) == 1
    }

    fun checkIfCarritosWasAddedToMainCarrito(db: SQLiteDatabase, idCarritos: List<Int>): List<Int> {
        val addedCarritos: MutableList<Int> = mutableListOf()

        db.query(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            null,
            "${Literals.Database.ColumnNames.ID_COLUMN} IN (${idCarritos.joinToString(",")})",
            null,
            null,
            null,
            null
        ).use {
            while (it.moveToNext()) {
                addedCarritos.add(
                    it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN))
                )
            }
        }

        return addedCarritos
    }

}
