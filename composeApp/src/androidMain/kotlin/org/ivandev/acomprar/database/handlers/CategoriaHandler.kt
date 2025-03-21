package org.ivandev.acomprar.database.handlers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.database.commands.categoria.GetAllCategoria
import org.ivandev.acomprar.database.commands.categoria.InsertIntoCategoria
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.interfaces.DatabaseCRUD

class CategoriaHandler: DatabaseCRUD<Categoria>
{
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }



    override fun create() {}
    override fun read() {}
    override fun update() {}
    override fun delete() {}

    override fun insert(db: SQLiteDatabase, obj: Categoria): Boolean {
        // Llamamos a la función de inserción
        InsertIntoCategoria(db, obj)

        // Verificamos si la fila fue insertada correctamente mediante la obtención del último ID
        val cursor = db.rawQuery("SELECT * FROM categoria ORDER BY id DESC LIMIT 1;", null)
        val inserted: Boolean

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))

            // Comparamos si el nombre coincide, lo cual indica que el objeto fue insertado correctamente
            inserted = nombre == obj.nombre
        } else {
            inserted = false
        }

        // No olvides cerrar el cursor
        cursor.close()

        // Retornamos el resultado de la inserción
        return inserted
    }

    override fun getAll(db: SQLiteDatabase): MutableList<Categoria> {
        return GetAllCategoria(db)
    }
}