package org.ivandev.acomprar.database.interfaces

import android.database.sqlite.SQLiteDatabase

interface DatabaseCRUD<T> {
    fun create()
    fun read()
    fun update()
    fun delete()
    fun insert(db: SQLiteDatabase, obj: T)

    fun getAll(): List<T>
}