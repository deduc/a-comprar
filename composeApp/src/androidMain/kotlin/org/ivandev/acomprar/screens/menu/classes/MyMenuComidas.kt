package org.ivandev.acomprar.screens.menu.classes

import org.ivandev.acomprar.database.entities.ComidaEntity


class MyMenuComidas (
    var menuId: Int?,
    var menuName: String?,
    var comidaEntities: List<ComidaEntity>,
)