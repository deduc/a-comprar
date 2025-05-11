package org.ivandev.acomprar.models

data class Comida(
    var id: Int?,
    var nombre: String,
    // TipoComidaEnum
    var tipo: Int,
)