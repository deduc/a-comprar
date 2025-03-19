package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object CreateTables {
    val CREATE_TABLE_CARRITO: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.CARRITO_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.DESCRIPTION_COLUMN} TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_CATEGORIA: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.CATEGORIA_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_MENU: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.MENU_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_PRODUCTO: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.PRODUCTO_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.ID_CATEGORIA_COLUMN} INTEGER,
            ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL,
            ${Literals.Database.CANTIDAD_COLUMN} FLOAT,
            ${Literals.Database.UNIDAD_CANTIDAD_COLUMN} TEXT,
            ${Literals.Database.MARCA_COLUMN} TEXT,
            FOREIGN KEY (${Literals.Database.ID_CATEGORIA_COLUMN}) 
                REFERENCES ${Literals.Database.CATEGORIA_TABLE}(${Literals.Database.ID_COLUMN}) 
                ON DELETE SET NULL
        );
    """.trimIndent()

    val CREATE_TABLE_CARRITO_PRODUCTO: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.CARRITO_PRODUCTO_TABLE} (
            ${Literals.Database.ID_CARRITO_COLUMN} INTEGER NOT NULL,
            ${Literals.Database.ID_PRODUCTO_COLUMN} INTEGER NOT NULL,
            PRIMARY KEY (
                ${Literals.Database.ID_CARRITO_COLUMN}, 
                ${Literals.Database.ID_PRODUCTO_COLUMN}
            ),
            FOREIGN KEY (${Literals.Database.ID_CARRITO_COLUMN}) 
                REFERENCES ${Literals.Database.CARRITO_TABLE}(${Literals.Database.ID_COLUMN}) 
                ON DELETE CASCADE,
            FOREIGN KEY (${Literals.Database.ID_PRODUCTO_COLUMN}) 
                REFERENCES ${Literals.Database.PRODUCTO_TABLE}(${Literals.Database.ID_COLUMN}) 
                ON DELETE CASCADE
        );
    """.trimIndent()
}
