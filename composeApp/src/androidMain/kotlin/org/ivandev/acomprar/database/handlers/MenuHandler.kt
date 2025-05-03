package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek

object MenuHandler  {
    fun addMenuDays(db: SQLiteDatabase, menuId: Int, menuDays: List<MenuDaysOfWeek>): Boolean {
        var result = true

        for (menuDay in menuDays) {
            val datos = ContentValues().apply {
                put(Literals.Database.ID_MENU_COLUMN, menuId)
                put(Literals.Database.DIA_COLUMN, menuDay.day)
                // Si en el futuro quieres añadir idComida o idCena, descomenta:
                // put("idComida", menuDay.idComida)
                // put("idCena", menuDay.idCena)
            }

            val insertResult = db.insert(Literals.Database.MENU_DAYS_OF_WEEK, null, datos)
            if (insertResult == -1L) {
                result = false
                break
            }
        }

        return result
    }

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


    fun delete(db: SQLiteDatabase, menuEntity: MenuEntity): Boolean {
        val result =  db.delete(
            Literals.Database.MENU_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(menuEntity.id.toString())
        )

        return result == 1
    }

    fun deleteLast(db: SQLiteDatabase): Boolean {
        var result = false
        val lastMenu: MenuEntity? = getLast(db)

        if (lastMenu != null) {
            val deletedRows: Int = db.delete(
                Literals.Database.MENU_TABLE,
                "${Literals.Database.ID_COLUMN} = ?",
                arrayOf(lastMenu.id.toString())
            )

            result = deletedRows == 1
        }

        return result
    }

    fun updateMenuNameById(db: SQLiteDatabase, menu: MenuEntity): Boolean {
        val datos = ContentValues()
        datos.put(Literals.Database.ID_COLUMN, menu.id)
        datos.put(Literals.Database.NOMBRE_COLUMN, menu.nombre)

        val result = db.update(
            Literals.Database.MENU_TABLE,
            datos,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf(menu.id.toString())
        )

        return result == 1
    }
}