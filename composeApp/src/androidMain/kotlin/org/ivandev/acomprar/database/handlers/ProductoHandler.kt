package org.ivandev.acomprar.database.handlers

import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.database.MySQLiteDatabase
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.interfaces.DatabaseCRUD

object ProductoHandler: DatabaseCRUD<Producto> {
    override fun create() {}
    override fun read() {}
    override fun update() {}
    override fun delete() {}

    override fun insert(db: SQLiteDatabase, obj: Producto) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Producto> {
        TODO("Not yet implemented")
    }
}