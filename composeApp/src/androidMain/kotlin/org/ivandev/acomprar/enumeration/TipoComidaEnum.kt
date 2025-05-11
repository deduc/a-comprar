package org.ivandev.acomprar.enumeration

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import org.ivandev.acomprar.database.entities.ComidaEntity

object TipoComidaEnum {
    const val COMIDA = 0
    const val CENA = 1
    const val MERIENDA = 2
    const val POSTRE = 3
    const val DESAYUNO = 4

    const val comidaStr = "Comida"
    const val cenaStr = "Cena"
    const val meriendaStr = "Merienda"
    const val postreStr = "Postre"
    const val desayunoStr = "Desayuno"

    fun getTiposInt(): List<Int> {
        return listOf(COMIDA, CENA, MERIENDA, POSTRE, DESAYUNO)
    }
    fun getTiposString(): List<String> {
        return listOf(comidaStr, cenaStr, meriendaStr, postreStr, desayunoStr)
    }

    fun getTipoComidaById(id: Int): String {
        return getTiposString()[id]
    }

    fun getTipoComidaPluralById(id: Int): String {
        return getTiposString()[id] + "s"
    }

    fun getComidasFilteredByTipo(comidasList: List<ComidaEntity>): SnapshotStateList<SnapshotStateList<ComidaEntity>> {
        return mutableStateListOf(
            comidasList.filter { it.tipo == COMIDA }.toMutableStateList(),
            comidasList.filter { it.tipo == CENA }.toMutableStateList(),
            comidasList.filter { it.tipo == MERIENDA }.toMutableStateList(),
            comidasList.filter { it.tipo == POSTRE }.toMutableStateList(),
            comidasList.filter { it.tipo == DESAYUNO }.toMutableStateList()
        )
    }
}