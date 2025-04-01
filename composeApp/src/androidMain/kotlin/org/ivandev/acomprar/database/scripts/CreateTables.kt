package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object CreateTables {
    val CREATE_TABLE_CARRITO: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.CARRITO_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.DESCRIPTION_COLUMN} TEXT NOT NULL
        );""".trimIndent()

    val CREATE_TABLE_CATEGORIA: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.CATEGORIA_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL
        );""".trimIndent()

    val CREATE_TABLE_COMIDA: String = """
    CREATE TABLE IF NOT EXISTS ${Literals.Database.COMIDA_TABLE} (
        ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Literals.Database.ID_MENU_COLUMN} INTEGER,
        ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL,
        ${Literals.Database.DIA_COLUMN} INTEGER NOT NULL CHECK(${Literals.Database.DIA_COLUMN} BETWEEN 0 AND 6),
        ${Literals.Database.TIPO_COLUMN} BOOLEAN NOT NULL CHECK(${Literals.Database.TIPO_COLUMN} IN (0,1)),
        FOREIGN KEY(${Literals.Database.ID_MENU_COLUMN}) REFERENCES ${Literals.Database.MENU_TABLE}(${Literals.Database.ID_COLUMN}) ON DELETE CASCADE
    );""".trimIndent()

    val CREATE_TABLE_COMIDA_PRODUCTO: String = """
    CREATE TABLE IF NOT EXISTS ${Literals.Database.COMIDA_PRODUCTO_TABLE} (
        ${Literals.Database.ID_COMIDA_COLUMN} INTEGER NOT NULL,
        ${Literals.Database.ID_PRODUCTO_COLUMN} INTEGER NOT NULL,
        PRIMARY KEY(${Literals.Database.ID_COMIDA_COLUMN}, ${Literals.Database.ID_PRODUCTO_COLUMN}),
        FOREIGN KEY(${Literals.Database.ID_COMIDA_COLUMN}) REFERENCES ${Literals.Database.COMIDA_TABLE}(${Literals.Database.ID_COLUMN}) ON DELETE CASCADE,
        FOREIGN KEY(${Literals.Database.ID_PRODUCTO_COLUMN}) REFERENCES ${Literals.Database.PRODUCTO_TABLE}(${Literals.Database.ID_COLUMN}) ON DELETE CASCADE
    );""".trimIndent()

    val CREATE_TABLE_MENU: String = """
        CREATE TABLE IF NOT EXISTS ${Literals.Database.MENU_TABLE} (
            ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Literals.Database.NOMBRE_COLUMN} TEXT NOT NULL
        );""".trimIndent()

    val CREATE_TABLE_MENU_COMIDA: String = """
    CREATE TABLE IF NOT EXISTS ${Literals.Database.MENU_COMIDA_TABLE} (
        ${Literals.Database.ID_COLUMN} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Literals.Database.ID_MENU_COLUMN} INTEGER NOT NULL,
        ${Literals.Database.ID_COMIDA_COLUMN} INTEGER NOT NULL,
        FOREIGN KEY(${Literals.Database.ID_MENU_COLUMN}) REFERENCES ${Literals.Database.MENU_TABLE}(${Literals.Database.ID_COLUMN}) ON DELETE CASCADE,
        FOREIGN KEY(${Literals.Database.ID_COMIDA_COLUMN}) REFERENCES ${Literals.Database.COMIDA_TABLE}(${Literals.Database.ID_COLUMN}) ON DELETE CASCADE
    );""".trimIndent()

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
        );""".trimIndent()

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
        );""".trimIndent()
}
