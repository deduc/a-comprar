package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.models.Categoria

object CategoriaHandler {
    fun insert(db: SQLiteDatabase, categoria: Categoria): Boolean {
        val datos = ContentValues()
        datos.put("nombre", categoria.nombre)
        db.insert(Literals.Database.CATEGORIA_TABLE, null, datos)

        return checkIfCategoriaWasInserted(db, categoria)
    }

    fun getAll(db: SQLiteDatabase): MutableList<CategoriaEntity> {
        val result = mutableListOf<CategoriaEntity>()

        db.rawQuery(
            "SELECT * FROM ${Literals.Database.CATEGORIA_TABLE}",
            null
        )
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        result.add(
                            CategoriaEntity(
                                cursor.getInt(0),
                                cursor.getString(1)
                            )
                        )
                    } while (cursor.moveToNext())
                }
            }
        return result
    }

    fun deleteById(db: SQLiteDatabase, id: Int): Boolean {
        return db.delete(
            Literals.Database.CATEGORIA_TABLE,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${id}")
        ) == 1
    }

    fun initialize(db: SQLiteDatabase) {
        val categoriasList: List<String> = Literals.Database.Categorias.getDefaultCategorias()

        categoriasList.forEach { categoria: String ->
            val categoriaAux = ContentValues().apply {
                if (categoria == Literals.Database.Categorias.SIN_CATEGORIA) {
                    put(Literals.Database.ID_COLUMN, 0)
                }
                put(Literals.Database.NOMBRE_COLUMN, categoria)
            }

            db.insert(
                Literals.Database.CATEGORIA_TABLE,
                null,
                categoriaAux
            )
            println("[DEBUG] Categoría añadida")
        }
    }

    fun updateCategoriaById(db: SQLiteDatabase, categoriaEntity: CategoriaEntity): Boolean {
        var categoriaNameColumn = ContentValues()
        categoriaNameColumn.put(Literals.Database.NOMBRE_COLUMN, categoriaEntity.nombre)

        return db.update(
            Literals.Database.CATEGORIA_TABLE,
            categoriaNameColumn,
            "${Literals.Database.ID_COLUMN} = ?",
            arrayOf("${categoriaEntity.id}"),
        ) == 1
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