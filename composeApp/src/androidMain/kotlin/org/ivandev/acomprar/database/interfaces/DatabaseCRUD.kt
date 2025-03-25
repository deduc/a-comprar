package org.ivandev.acomprar.database.interfaces

import android.database.sqlite.SQLiteDatabase

interface DatabaseCRUD<T> {
    fun insert(db: SQLiteDatabase, obj: T): Boolean
    fun deleteById(db: SQLiteDatabase, id: Int): Boolean
    fun getAll(db: SQLiteDatabase): MutableList<T>
}