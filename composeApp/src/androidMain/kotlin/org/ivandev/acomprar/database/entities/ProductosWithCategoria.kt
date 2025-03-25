package org.ivandev.acomprar.database.entities

class ProductosWithCategoria(
    val categoriaName: String,
    val categoriaId: Int,
    val productos: List<Producto>?
)