package org.ivandev.acomprar.screens.comida.classes

import org.ivandev.acomprar.database.entities.ComidaEntity

class ComidasYCenasSeparatedLists(
    _comidas: MutableList<ComidaEntity?> = mutableListOf(),
    _cenas: MutableList<ComidaEntity?> = mutableListOf()
) {
    var comidas: MutableList<ComidaEntity?>
    var cenas: MutableList<ComidaEntity?>

    init {
        comidas = _comidas
        cenas = _cenas
    }
}