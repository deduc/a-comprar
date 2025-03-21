package org.ivandev.acomprar.database.commands.categoria

import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Categoria

fun GetAllCategoria(db: SQLiteDatabase): MutableList<Categoria> {
    val command = "SELECT * FROM ${Literals.Database.CATEGORIA_TABLE}"
    val result = mutableListOf<Categoria>()

    db.rawQuery(command, null).use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                result.add(
                    Categoria(
                        cursor.getInt(0),
                        cursor.getString(1)
                    )
                )
            } while (cursor.moveToNext())
        }
    }
    return result
}
