package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData

object MainCarritoHandler {
    fun addCarritoToMainCarrito(db: SQLiteDatabase, carritoId: Int): Boolean {
        var carritoProductos: CarritoAndProductsData = CarritoHandler.getCarritoAndProductosByCarritoId(db, carritoId)

        val values = ContentValues().apply {
            put(Literals.Database.ID_COLUMN, Literals.Database.MAIN_CARRITO_ID)
            put(Literals.Database.ID_PRODUCTO_COLUMN, carritoId)
            put(Literals.Database.CANTIDAD_COLUMN, carritoId)
            put(Literals.Database.CANTIDAD_COLUMN, carritoId)
        }

        return true
    }
}