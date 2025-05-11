package org.ivandev.acomprar.enumeration

import org.ivandev.acomprar.database.entities.ComidaEntity

object TipoComidaEnum {
    const val COMIDA = 0
    const val CENA = 1
    const val MERIENDA = 2
    const val POSTRE = 3

    const val comidaStr = "Comida"
    const val cenaStr = "Cena"
    const val meriendaStr = "Merienda"
    const val postreStr = "Postre"

    fun getTiposInt(): List<Int> {
        return listOf(COMIDA, CENA, MERIENDA, POSTRE)
    }
    fun getTiposString(): List<String> {
        return listOf(comidaStr, cenaStr, meriendaStr, postreStr)
    }

    fun getTipoComidaById(id: Int): String {
        return getTiposString()[id]
    }

    fun getTipoComidaPluralById(id: Int): String {
        return getTiposString()[id] + "s"
    }

    fun separateComidas(comidasList: List<ComidaEntity>): List<List<ComidaEntity>> {
        return listOf(
            comidasList.filter { it.tipo == TipoComidaEnum.COMIDA },
            comidasList.filter { it.tipo == TipoComidaEnum.CENA },
            comidasList.filter { it.tipo == TipoComidaEnum.MERIENDA },
            comidasList.filter { it.tipo == TipoComidaEnum.POSTRE }
        )
    }
}