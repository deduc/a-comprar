package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.MenuEntity

object MenuHandler  {
    fun getAll(db: SQLiteDatabase): List<MenuEntity> {
        val command = "SELECT * FROM ${Literals.Database.MENU_TABLE}"
        val result = mutableListOf<MenuEntity>()

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    result.add(
                        MenuEntity(
                            cursor.getInt(0),
                            cursor.getString(1)
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return result
    }

    fun getLast(db: SQLiteDatabase): MenuEntity? {
        val command = "SELECT * FROM ${Literals.Database.MENU_TABLE} ORDER BY ${Literals.Database.ID_COLUMN} DESC LIMIT 1"
        var result: MenuEntity? = null

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                result = MenuEntity(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ID_COLUMN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.NOMBRE_COLUMN)),
                )
            }
        }

        return result
    }

    fun insert(db: SQLiteDatabase, menuEntity: MenuEntity): Boolean {
        val datos = ContentValues()
        datos.put(Literals.Database.NOMBRE_COLUMN, menuEntity.nombre)

        val result = db.insert(
            Literals.Database.MENU_TABLE,
            null,
            datos
        )

        return result >= 1
    }


    fun delete(db: SQLiteDatabase, menuEntity: MenuEntity): Boolean {
        val result =  db.delete(
            Literals.Database.MENU_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(menuEntity.id.toString())
        )

        return result == 1
    }

}