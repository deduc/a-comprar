package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.MainCarritoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum

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
        return db.delete(
            Literals.Database.Tables.MAIN_CARRITO_TABLE,
            "${Literals.Database.ColumnNames.ID_CARRITO_COLUMN} = ?",
            arrayOf(id.toString())
        ) > 0
    }
}
