package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getIntOrNull
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek

object MenuHandler  {
    fun addMenuDays(db: SQLiteDatabase, menuId: Int, menuDays: List<MenuDaysOfWeek>): Boolean {
        var result = true

        for (menuDay in menuDays) {
            val datos = ContentValues().apply {
                put(Literals.Database.ColumnNames.ID_COLUMN, menuDay.id)
                put(Literals.Database.ColumnNames.ID_MENU_COLUMN, menuDay.idMenu)
                put(Literals.Database.ColumnNames.ID_COMIDA_COLUMN, menuDay.idComida)
                put(Literals.Database.ColumnNames.TIPO_COLUMN, menuDay.tipoComida)
                put(Literals.Database.ColumnNames.DIA_COLUMN, menuDay.day)
            }

            val insertResult = db.insert(Literals.Database.Tables.MENU_DAYS_OF_WEEK, null, datos)

            if (insertResult == -1L) {
                result = false
                break
            }
        }

        return result
    }

    fun getAll(db: SQLiteDatabase): List<MenuEntity> {
        val result = mutableListOf<MenuEntity>()

        db.rawQuery(
            "SELECT * FROM ${Literals.Database.Tables.MENU_TABLE}",
            null
        ).use { cursor ->
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
        var result: MenuEntity? = null

        db.rawQuery(
            "SELECT * FROM ${Literals.Database.Tables.MENU_TABLE} ORDER BY ${Literals.Database.ColumnNames.ID_COLUMN} DESC LIMIT 1",
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                result = MenuEntity(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.NOMBRE_COLUMN)),
                )
            }
        }

        return result
    }

    fun getMenuDaysOfWeekByMenuId(db: SQLiteDatabase, menuId: Int): MutableList<MenuDaysOfWeekEntity> {
        var result = mutableListOf<MenuDaysOfWeekEntity>()
        val command =
                "SELECT * " +
                "FROM ${Literals.Database.Tables.MENU_DAYS_OF_WEEK} " +
                "where ${Literals.Database.ColumnNames.ID_MENU_COLUMN} == ${menuId}"

        db.rawQuery(command, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    result.add(
                        MenuDaysOfWeekEntity(
                            cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_MENU_COLUMN)),
                            cursor.getIntOrNull(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COMIDA_COLUMN)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.TIPO_COLUMN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Literals.Database.ColumnNames.DIA_COLUMN))
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        return result
    }

    fun insert(db: SQLiteDatabase, menu: Menu): Boolean {
        val datos = ContentValues()
        datos.put(Literals.Database.ColumnNames.NOMBRE_COLUMN, menu.nombre)

        return db.insert(
            Literals.Database.Tables.MENU_TABLE,
            null,
            datos
        ) >= 1
    }


    fun delete(db: SQLiteDatabase, menuEntity: MenuEntity): Boolean {
        return db.delete(
            Literals.Database.Tables.MENU_TABLE,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(menuEntity.id.toString())
        ) == 1
    }

    fun deleteLast(db: SQLiteDatabase): Boolean {
        var result = false
        val lastMenu: MenuEntity? = getLast(db)

        if (lastMenu != null) {
            val deletedRows: Int = db.delete(
                Literals.Database.Tables.MENU_TABLE,
                "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
                arrayOf(lastMenu.id.toString())
            )

            result = deletedRows == 1
        }

        return result
    }

    fun updateMenuNameById(db: SQLiteDatabase, menu: MenuEntity): Boolean {
        val datos = ContentValues()
        datos.put(Literals.Database.ColumnNames.ID_COLUMN, menu.id)
        datos.put(Literals.Database.ColumnNames.NOMBRE_COLUMN, menu.nombre)

        return db.update(
            Literals.Database.Tables.MENU_TABLE,
            datos,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(menu.id.toString())
        ) == 1
    }

    fun updateMenuDaysOfWeekById(db: SQLiteDatabase, menuDaysOfWeek: MenuDaysOfWeek): Boolean {
        val datos = ContentValues()

        datos.put(Literals.Database.ColumnNames.ID_COLUMN, menuDaysOfWeek.id)
        datos.put(Literals.Database.ColumnNames.ID_MENU_COLUMN, menuDaysOfWeek.idMenu)
        datos.put(Literals.Database.ColumnNames.ID_COMIDA_COLUMN, menuDaysOfWeek.idComida)
        datos.put(Literals.Database.ColumnNames.TIPO_COLUMN, menuDaysOfWeek.tipoComida)
        datos.put(Literals.Database.ColumnNames.DIA_COLUMN, menuDaysOfWeek.day)

        val updatedRows = db.update(
            Literals.Database.Tables.MENU_DAYS_OF_WEEK,
            datos,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(menuDaysOfWeek.id!!.toString())
        )

        return updatedRows == 1
    }
}