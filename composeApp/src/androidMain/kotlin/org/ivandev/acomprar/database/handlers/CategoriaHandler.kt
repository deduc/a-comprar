package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.interfaces.DatabaseCRUD

object CategoriaHandler: DatabaseCRUD<Categoria> {
    override fun insert(db: SQLiteDatabase, categoria: Categoria): Boolean {
        val datos = ContentValues()
        datos.put("nombre", categoria.nombre)
        db.insert(Literals.Database.CATEGORIA_TABLE, null, datos)

        return checkIfCategoriaWasInserted(db, categoria)
    }

    override fun getAll(db: SQLiteDatabase): MutableList<Categoria> {
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

    override fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        val deletedRows: Int = db.delete(
            Literals.Database.CATEGORIA_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${id}")
        )

        return deletedRows == 1
    }

    private fun checkIfCategoriaWasInserted(db: SQLiteDatabase, categoria: Categoria): Boolean {
        db.query(
            Literals.Database.CATEGORIA_TABLE, // Nombre tabla
            arrayOf(Literals.Database.NOMBRE_COLUMN), // Columnas output
            null, // condicion where
            null, // placeholder para evitar SQL inyection
            null, // group by columnas
            null, // having del group by
            "id DESC", // ordenar salida
            "1" // cantidad de filas retornadas
        ).use { cursor ->
            return cursor.moveToFirst() && cursor.getString(0) == categoria.nombre
        }
    }
}