package org.ivandev.acomprar.screens.menu.classes

import androidx.compose.runtime.MutableState
import org.ivandev.acomprar.database.entities.ComidaEntity

class MyDropdownMenuData(
    var comidaOrCenaTitle: String,
    var expanded: MutableState<Boolean>,
    var comidaSelected: MutableState<ComidaEntity?>,
    var isComida: Boolean,
    var comidasByTipo: List<ComidaEntity?>
)