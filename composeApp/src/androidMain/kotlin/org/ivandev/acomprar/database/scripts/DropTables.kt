package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object DropTables {
    val DROP_TABLE_CARRITO: String = "DROP TABLE IF EXISTS ${Literals.Database.CARRITO_TABLE};"
    val DROP_TABLE_CATEGORIA: String = "DROP TABLE IF EXISTS ${Literals.Database.CATEGORIA_TABLE};"
    val DROP_TABLE_MENU: String = "DROP TABLE IF EXISTS ${Literals.Database.MENU_TABLE};"
    val DROP_TABLE_PRODUCTO: String = "DROP TABLE IF EXISTS ${Literals.Database.PRODUCTO_TABLE};"
    val DROP_TABLE_CARRITO_PRODUCTO: String = "DROP TABLE IF EXISTS ${Literals.Database.CARRITO_PRODUCTO_TABLE}"
}