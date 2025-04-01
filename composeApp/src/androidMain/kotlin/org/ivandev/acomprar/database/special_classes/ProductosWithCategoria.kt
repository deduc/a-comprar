package org.ivandev.acomprar.database.special_classes

import org.ivandev.acomprar.database.entities.Producto

class ProductosWithCategoria(
    val categoriaName: String,
    val categoriaId: Int,
    val productos: List<Producto>?
)