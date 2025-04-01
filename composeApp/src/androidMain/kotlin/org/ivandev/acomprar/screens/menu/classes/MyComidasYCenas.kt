package org.ivandev.acomprar.screens.menu.classes

import org.ivandev.acomprar.database.entities.Comida


class MyComidasYCenas (
    var menuId: Int?,
    var menuName: String?,
    var comidas: List<Comida?>,
    var cenas: List<Comida?>
)