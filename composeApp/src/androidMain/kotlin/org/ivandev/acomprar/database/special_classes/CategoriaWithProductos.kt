package org.ivandev.acomprar.database.special_classes

import org.ivandev.acomprar.database.entities.ProductoEntity

class CategoriaWithProductos(
    val categoriaName: String,
    val categoriaId: Int,
    val productoEntities: List<ProductoEntity>
)