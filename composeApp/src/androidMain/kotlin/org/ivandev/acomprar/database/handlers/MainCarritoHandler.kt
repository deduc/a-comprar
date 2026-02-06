package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.MainCarritoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum
import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Categoria
import java.time.LocalDate

object MainCarritoHandler {
    fun initialize(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.ID_COLUMN, Literals.Database.HardcodedValues.MAIN_CARRITO_ID)
            put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, Literals.Database.HardcodedValues.MAIN_CARRITO_ID)
        }
        val inserted = db.insert(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            null,
            values
        )
        val b = inserted != -1L
    }

    fun addNotBoughtProductsIntoSpecialCarrito(db: SQLiteDatabase, items: List<ProductoEntity>): Boolean {
        val currentDateTime = Tools.getCurrentDateDDMMYYYY()
        val specialCarrito = Carrito(
            null,
            "${Literals.Database.HardcodedValues.CARRITO_ESPECIAL_NAME} $currentDateTime",
            "${Literals.Database.HardcodedValues.CARRITO_ESPECIAL_DESCRIPTION} $currentDateTime",
        )

        CarritoHandler.add(db, specialCarrito)
        deleteItemsByIds(db, items)

        val carrito: CarritoEntity? = CarritoHandler.getCarritoByNameDescription(db, specialCarrito.name, specialCarrito.description)
        if (carrito != null)
            return CarritoHandler.addLotProductos(db, carrito, items)
        else return false
    }

    fun getIdCarritosFromMainCarrito(db: SQLiteDatabase): List<Int> {
        var result = mutableListOf<Int>()

        db.query(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        ).use { it: Cursor ->
            if (it.moveToFirst()) {
                do {
                    result.add(
                        it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_CARRITO_COLUMN)),
                    )
                } while (it.moveToNext())
            }
        }

        return result
    }
    fun addCarritoToMainCarrito(db: SQLiteDatabase, carritoId: Int): Boolean {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.ID_COLUMN, Literals.Database.HardcodedValues.MAIN_CARRITO_ID)
            put(Literals.Database.ColumnNames.ID_CARRITO_COLUMN, carritoId)
        }

        val inserted: Long = db.insert(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            null,
            values
        )
        return inserted != -1L
    }

    fun deleteItemsByIds(db: SQLiteDatabase, items: List<ProductoEntity>): Boolean {
        if (items.isEmpty()) return true

        val placeholders = items.joinToString(",") { "?" }
        val args = items.map { it.id.toString() }.toTypedArray()

        return try {
            val rows = db.delete(
                Literals.Database.Tables.MAIN_CARRITO_WITH_PRODUCTS,
                "${Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN} IN ($placeholders)",
                args
            )
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getCarritosAddedToMainCarrito(db: SQLiteDatabase): List<CarritoEntity> {
        var result = mutableListOf<CarritoEntity>()

        val idCarritoList: List<Int> = getIdCarritosFromMainCarrito(db)
        val selectionArgs = idCarritoList.map { it.toString() }.toTypedArray()
        val placeholders = idCarritoList.joinToString(",") { "?" }
        val selection = "${Literals.Database.ColumnNames.ID_COLUMN} IN ($placeholders)"

        db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                do {
                    val carrito = CarritoEntity(
                        id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                        name = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                        description = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.DESCRIPTION_COLUMN)),
                    )
                    result.add(carrito)
                } while (it.moveToNext())
            }
        }
        return result
    }

    fun deleteCarritoFromMainCarrito(db: SQLiteDatabase, id: Int): Boolean {
        val result1 = db.delete(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ?",
            arrayOf(id.toString())
        ) > 0

        val result2 = db.delete(
            Literals.Database.Tables.MAIN_CARRITO_WITH_PRODUCTS,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(id.toString())
        ) > 0

        return result1 == result2
    }

    fun deleteFromCarritoBastardo(db: SQLiteDatabase): Boolean {
        val table = Literals.Database.Tables.CARRITO_PRODUCTO_TABLE
        val whereClause = "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ?"
        val whereArgs = arrayOf(Literals.Database.HardcodedValues.CARRITO_BASTARDO_ID.toString())

        return try {
            val deletedRows = db.delete(table, whereClause, whereArgs)
            // delete() retorna número de filas eliminadas, 0 si no eliminó nada
            deletedRows >= 0
        } catch (e: Exception) {
            // Log o manejo de error según tu necesidad
            false
        }
    }

    fun getAllCarritoFromMainCarrito(db: SQLiteDatabase): List<CarritoEntity> {
        val carritos: MutableList<CarritoEntity> = mutableListOf()

        db.query(
            Literals.Database.Tables.CARRITO_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        ).use {
            if (it.moveToFirst()) {
                do {
                    carritos.add(
                        CarritoEntity(
                            id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                            name = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                            description = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.DESCRIPTION_COLUMN)),
                        )
                    )
                } while (it.moveToNext())
            }
        }

        return carritos.toList()
    }

    fun loadAndInsertCarritosToBuyList(db: SQLiteDatabase): List<CategoriaWithProductos> {
        val result: MutableList<CategoriaWithProductos> = mutableListOf()
        var insertResult: Boolean = false
        val allCarritoProductos: List<CarritoAndProductsData> = getAllProducts(db)
        val allProductosAux: List<Pair<ProductoEntity, Int>> = allCarritoProductos.flatMap { it.productosAndCantidades }

        val allProductosFixed: List<Pair<ProductoEntity, Int>> = mergeProductos(allProductosAux)
        val allCategorias: List<CategoriaEntity> = CategoriaHandler.getAll(db)

        allCategorias.forEach { categoria: CategoriaEntity ->
            result.add(
                CategoriaWithProductos(
                    categoria.nombre,
                    categoria.id,
                    allProductosFixed.map { it.first }.filter { it.idCategoria == categoria.id }
                )
            )
        }

        insertResult = insertCarritosToBuy(db, allProductosFixed)
        if (! insertResult) {
            println("[DEBUG] Error desconocido al insertar los productos a la tabla MainCarritoWithProducts")
        }

        return result.toList()
    }

    fun loadCarritosToBuyList(db: SQLiteDatabase): List<CategoriaWithProductos> {
        val result: MutableList<CategoriaWithProductos> = mutableListOf()
        var insertResult: Boolean = false
        val allCarritoProductos: List<CarritoAndProductsData> = getAllProducts(db)
        val allProductosAux: List<Pair<ProductoEntity, Int>> = allCarritoProductos.flatMap { it.productosAndCantidades }

        val allProductosFixed: List<Pair<ProductoEntity, Int>> = mergeProductos(allProductosAux)
        val allCategorias: List<CategoriaEntity> = CategoriaHandler.getAll(db)

        allCategorias.forEach { categoria: CategoriaEntity ->
            result.add(
                CategoriaWithProductos(
                    categoria.nombre,
                    categoria.id,
                    allProductosFixed.map { it.first }.filter { it.idCategoria == categoria.id }
                )
            )
        }

        return result.toList()
    }

    private fun insertCarritosToBuy(db: SQLiteDatabase, allProductosFixed: List<Pair<ProductoEntity, Int>>): Boolean {
        var result: Boolean = true

        allProductosFixed.forEach {
            val values = ContentValues().apply {
                put(Literals.Database.ColumnNames.ID_COLUMN, Literals.Database.HardcodedValues.CARRITO_BASTARDO_ID)
                put(Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN, it.first.id)
                put(Literals.Database.ColumnNames.CANTIDAD_COLUMN, it.second)
            }
            result = db.insert(
                Literals.Database.Tables.MAIN_CARRITO_WITH_PRODUCTS,
                null,
                values
            ) != -1L
            if (!result) { return false }
        }
        return true
    }

    private fun mergeProductos(productos: List<Pair<ProductoEntity, Int>>): List<Pair<ProductoEntity, Int>> {
        return productos
            .groupBy { it.first.id }
            .map { (_, lista) ->
                val producto = lista.first().first
                val cantidadTotal = lista.sumOf { it.second }
                producto to cantidadTotal
            }
    }


    private fun getAllProducts(db: SQLiteDatabase): List<CarritoAndProductsData> {
        val carritos: List<CarritoEntity> = getAllCarritoFromMainCarrito(db)
        val carritoIds: List<Int> = carritos.map { it.id }

        val allProductos: MutableList<CarritoAndProductsData> = mutableListOf()

        carritoIds.forEach { id: Int ->
            allProductos.add(
                CarritoHandler.getCarritoAndProductosByCarritoId(db, id)
            )
        }
        return allProductos.toList()
    }
}
