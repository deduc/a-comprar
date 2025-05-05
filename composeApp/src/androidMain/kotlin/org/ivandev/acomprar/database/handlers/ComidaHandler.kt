package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.enumeration.DaysOfWeekEnum
import org.ivandev.acomprar.enumeration.TipoComidaEnum

object ComidaHandler {
    fun getComidasByMenuId(db: SQLiteDatabase, id: Int): List<ComidaEntity> {
        val command = "SELECT * FROM ${Literals.Database.COMIDA_TABLE} where ${Literals.Database.ID_MENU_COLUMN} = $id"
        val result = mutableListOf<ComidaEntity>()

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    result.add(
                        ComidaEntity(
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

    fun insertComidasYCenasByMenuId(db: SQLiteDatabase, menuId: Int): Boolean {
        var result = false

        try {
            db.beginTransaction()
            val lunes = DaysOfWeekEnum.LUNES
            val domingo = DaysOfWeekEnum.DOMINGO

            val values = ContentValues()
            for (dia in lunes..domingo) {
                // Insertar comida
                values.apply {
                    clear()
                    put(Literals.Database.ID_MENU_COLUMN, menuId)
                    put(Literals.Database.NOMBRE_COLUMN, Literals.Database.VOID_STR)
                    put(Literals.Database.DIA_COLUMN, dia)
                    put(Literals.Database.TIPO_COLUMN, TipoComidaEnum.COMIDA)
                }
                db.insert(Literals.Database.COMIDA_TABLE, null, values)

                // Insertar cena
                values.apply {
                    clear()
                    put(Literals.Database.ID_MENU_COLUMN, menuId)
                    put(Literals.Database.NOMBRE_COLUMN, Literals.Database.VOID_STR)
                    put(Literals.Database.DIA_COLUMN, dia)
                    put(Literals.Database.TIPO_COLUMN, TipoComidaEnum.CENA)
                }
                db.insert(Literals.Database.COMIDA_TABLE, null, values)
            }

            db.setTransactionSuccessful() // Confirmar la transacción
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction() // Finalizar la transacción (commit o rollback)
        }

        return result
    }

    fun deleteAllComidasByMenuId(db: SQLiteDatabase, menuId: Int): Boolean {
        val result = db.delete(
            Literals.Database.COMIDA_TABLE,
            "${Literals.Database.ID_MENU_COLUMN} = ?",
            arrayOf(menuId.toString())
        )

        return result >= 0
    }
}