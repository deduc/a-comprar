package org.ivandev.acomprar.screens.comida.classes

import org.ivandev.acomprar.database.entities.ComidaEntity

class ComidasYCenasSeparatedLists(
    _comidas: List<ComidaEntity?> = listOf(),
    _cenas: List<ComidaEntity?> = listOf()
) {
    var comidas: List<ComidaEntity?>
    var cenas: List<ComidaEntity?>

    init {
        comidas = _comidas
        cenas = _cenas
    }
}