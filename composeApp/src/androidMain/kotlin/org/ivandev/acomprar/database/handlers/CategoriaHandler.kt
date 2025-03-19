package org.ivandev.acomprar.database.handlers

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.interfaces.DatabaseCRUD

@SuppressLint("StaticFieldLeak")
object CategoriaHandler: DatabaseCRUD<Categoria> {
    private const val FILE_NAME: String = Literals.Database.CATEGORIA_FILE

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context

    fun setContext(context: Context) {
        this.context = context
    }



    override fun create() {}
    override fun read() {}
    override fun update() {}
    override fun delete() {}

    override fun insert(db: SQLiteDatabase, obj: Categoria) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Categoria> {
        var categorias: List<Categoria> = listOf()
        return categorias
    }
}