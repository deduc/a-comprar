package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object DropTables {
    // Tablas
    val tableCarrito = Literals.Database.CARRITO_TABLE
    val tableCategoria = Literals.Database.CATEGORIA_TABLE
    val tableComida = Literals.Database.COMIDA_TABLE
    val tableComidaProducto = Literals.Database.COMIDA_PRODUCTO_TABLE
    val tableMenu = Literals.Database.MENU_TABLE
    val tableMenuComida = Literals.Database.MENU_COMIDA_TABLE
    val tableProducto = Literals.Database.PRODUCTO_TABLE
    val tableCarritoProducto = Literals.Database.CARRITO_PRODUCTO_TABLE
    val tableMenuDaysOfWeek = Literals.Database.MENU_DAYS_OF_WEEK
    val tableMainCarrito = Literals.Database.MAIN_CARRITO_TABLE


    // Sentencias DROP TABLE
    val DROP_TABLE_CARRITO = "DROP TABLE IF EXISTS $tableCarrito;"
    val DROP_TABLE_CATEGORIA = "DROP TABLE IF EXISTS $tableCategoria;"
    val DROP_TABLE_COMIDA = "DROP TABLE IF EXISTS $tableComida;"
    val DROP_TABLE_COMIDA_PRODUCTO = "DROP TABLE IF EXISTS $tableComidaProducto;"
    val DROP_TABLE_MENU = "DROP TABLE IF EXISTS $tableMenu;"
    val DROP_TABLE_MENU_COMIDA = "DROP TABLE IF EXISTS $tableMenuComida;"
    val DROP_TABLE_PRODUCTO = "DROP TABLE IF EXISTS $tableProducto;"
    val DROP_TABLE_CARRITO_PRODUCTO = "DROP TABLE IF EXISTS $tableCarritoProducto;"
    val DROP_TABLE_MENU_DAYS_OF_WEEK = "DROP TABLE IF EXISTS $tableMenuDaysOfWeek;"
    val DROP_TABLE_CARRITO_CONTENIDO = "DROP TABLE IF EXISTS $tableMainCarrito"
}
