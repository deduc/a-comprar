package org.ivandev.acomprar.database.commands.categoria

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Categoria

fun InsertIntoCategoria(db: SQLiteDatabase, categoria: Categoria) {
    val datos = ContentValues()

    datos.put("nombre", categoria.nombre)

    // No cierres la base de datos aquí, deja que la lógica fuera de esta función la cierre
    db.insert(Literals.Database.CATEGORIA_TABLE, null, datos)
}
