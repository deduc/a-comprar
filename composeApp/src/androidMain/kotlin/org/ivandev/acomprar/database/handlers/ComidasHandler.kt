package org.ivandev.acomprar.database.handlers

import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Comida

object ComidasHandler {
    fun getComidasByMenuId(db: SQLiteDatabase, id: Int): List<Comida> {
        val command = "SELECT * FROM ${Literals.Database.COMIDA_TABLE} where menuId = $id"
        val result = mutableListOf<Comida>()

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    result.add(
                        Comida(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4),
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return result
    }

}