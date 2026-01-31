package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object DropTables {
    // Tablas
    val tableCarrito = Literals.Database.Tables.CARRITO_TABLE
    val tableCategoria = Literals.Database.Tables.CATEGORIA_TABLE
    val tableComida = Literals.Database.Tables.COMIDA_TABLE
    val tableComidaProducto = Literals.Database.Tables.COMIDA_PRODUCTO_TABLE
    val tableMenu = Literals.Database.Tables.MENU_TABLE
    val tableMenuComida = Literals.Database.Tables.MENU_COMIDA_TABLE
    val tableProducto = Literals.Database.Tables.PRODUCTO_TABLE
    val tableCarritoProducto = Literals.Database.Tables.CARRITO_PRODUCTO_TABLE
    val tableMenuDaysOfWeek = Literals.Database.Tables.MENU_DAYS_OF_WEEK
    val tableMainCarrito = Literals.Database.Tables.MAIN_CARRITO_TABLE
    val tableMainCarritoWithProducts = Literals.Database.Tables.MAIN_CARRITO_WITH_PRODUCTS
    val tableUserActions = Literals.Database.Tables.USER_ACTIONS_TABLE


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
    val DROP_TABLE_MAIN_CARRITO = "DROP TABLE IF EXISTS $tableMainCarrito"
    val DROP_TABLE_MAIN_CARRITO_WITH_PRODUCTS = "DROP TABLE IF EXISTS $tableMainCarritoWithProducts"
    val DROP_TABLE_USER_ACTIONS = "DROP TABLE IF EXISTS $tableUserActions"
}
