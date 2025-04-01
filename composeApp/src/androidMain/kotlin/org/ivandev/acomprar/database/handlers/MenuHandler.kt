package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Menu

object MenuHandler  {
    fun getAll(db: SQLiteDatabase): List<Menu> {
        val command: String = "SELECT * FROM ${Literals.Database.MENU_TABLE}"
        val result = mutableListOf<Menu>()

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    result.add(
                        Menu(
                            cursor.getInt(0),
                            cursor.getString(1)
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return result
    }

    fun insert(db: SQLiteDatabase, menu: Menu): Boolean {
        val datos = ContentValues()
        datos.put(Literals.Database.NOMBRE_COLUMN, menu.nombre)

        val result = db.insert(
            Literals.Database.MENU_TABLE,
            null,
            datos
        )

        return result >= 1
    }


    fun delete(db: SQLiteDatabase, menu: Menu): Boolean {
        val result =  db.delete(
            Literals.Database.MENU_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(menu.id.toString())
        )

        return result == 1
    }

}