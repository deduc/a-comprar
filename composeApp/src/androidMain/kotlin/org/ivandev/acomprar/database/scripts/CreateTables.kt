package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object CreateTables {
    // Tablas
    val tableCarrito = Literals.Database.CARRITO_TABLE
    val tableCategoria = Literals.Database.CATEGORIA_TABLE
    val tableComida = Literals.Database.COMIDA_TABLE
    val tableProducto = Literals.Database.PRODUCTO_TABLE
    val tableMenu = Literals.Database.MENU_TABLE
    val tableComidaProducto = Literals.Database.COMIDA_PRODUCTO_TABLE
    val tableMenuComida = Literals.Database.MENU_COMIDA_TABLE
    val tableMenuDayOfWeek = Literals.Database.MENU_DAYS_OF_WEEK
    val tableCarritoProducto = Literals.Database.CARRITO_PRODUCTO_TABLE

    // Columnas
    val idColumn = Literals.Database.ID_COLUMN
    val descriptionColumn = Literals.Database.DESCRIPTION_COLUMN
    val nombreColumn = Literals.Database.NOMBRE_COLUMN
    val idMenuColumn = Literals.Database.ID_MENU_COLUMN
    val idComidaColumn = Literals.Database.ID_COMIDA_COLUMN
    val idCenaColumn = Literals.Database.ID_CENA_COLUMN
    val diaColumn = Literals.Database.DIA_COLUMN
    val tipoColumn = Literals.Database.TIPO_COLUMN
    val idProductoColumn = Literals.Database.ID_PRODUCTO_COLUMN
    val idCategoriaColumn = Literals.Database.ID_CATEGORIA_COLUMN
    val cantidadColumn = Literals.Database.CANTIDAD_COLUMN
    val marcaColumn = Literals.Database.MARCA_COLUMN
    val idCarritoColumn = Literals.Database.ID_CARRITO_COLUMN

    // Tablas
    val CREATE_TABLE_CARRITO = """
        CREATE TABLE IF NOT EXISTS $tableCarrito (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $descriptionColumn TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_CATEGORIA = """
        CREATE TABLE IF NOT EXISTS $tableCategoria (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $nombreColumn TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_COMIDA = """
        CREATE TABLE IF NOT EXISTS $tableComida (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $nombreColumn TEXT NOT NULL,
            $tipoColumn BOOLEAN NOT NULL CHECK($tipoColumn IN (0,1))
        );
    """.trimIndent()

    val CREATE_TABLE_COMIDA_PRODUCTO = """
        CREATE TABLE IF NOT EXISTS $tableComidaProducto (
            $idComidaColumn INTEGER NOT NULL,
            $idProductoColumn INTEGER NOT NULL,
            PRIMARY KEY($idComidaColumn, $idProductoColumn),
            FOREIGN KEY($idComidaColumn) REFERENCES $tableComida($idColumn) ON DELETE CASCADE,
            FOREIGN KEY($idProductoColumn) REFERENCES $tableProducto($idColumn) ON DELETE CASCADE
        );
    """.trimIndent()

    val CREATE_TABLE_MENU = """
        CREATE TABLE IF NOT EXISTS $tableMenu (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $nombreColumn TEXT NOT NULL
        );
    """.trimIndent()

    val CREATE_TABLE_MENU_COMIDA = """
        CREATE TABLE IF NOT EXISTS $tableMenuComida (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $idMenuColumn INTEGER NOT NULL,
            $idComidaColumn INTEGER NOT NULL,
            FOREIGN KEY($idMenuColumn) REFERENCES $tableMenu($idColumn) ON DELETE CASCADE,
            FOREIGN KEY($idComidaColumn) REFERENCES $tableComida($idColumn) ON DELETE CASCADE
        );
    """.trimIndent()

    val CREATE_TABLE_MENU_DAY_OF_WEEK = """
        CREATE TABLE IF NOT EXISTS $tableMenuDayOfWeek (
            $idColumn INTEGER PRIMARY KEY,
            $idMenuColumn INTEGER NOT NULL,
            $idComidaColumn INTEGER,
            $idCenaColumn INTEGER,
            $diaColumn TEXT NOT NULL,
            FOREIGN KEY ($idMenuColumn) REFERENCES $tableMenu($idColumn) ON DELETE CASCADE,
            FOREIGN KEY ($idComidaColumn) REFERENCES $tableComida($idColumn),
            FOREIGN KEY ($idCenaColumn) REFERENCES $tableComida($idColumn)
        );
    """.trimIndent()

    val CREATE_TABLE_PRODUCTO = """
        CREATE TABLE IF NOT EXISTS $tableProducto (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $idCategoriaColumn INTEGER,
            $nombreColumn TEXT NOT NULL,
            $cantidadColumn TEXT,
            $marcaColumn TEXT,
            FOREIGN KEY ($idCategoriaColumn) REFERENCES $tableCategoria($idColumn) ON DELETE SET NULL
        );
    """.trimIndent()

    val CREATE_TABLE_CARRITO_PRODUCTO = """
        CREATE TABLE IF NOT EXISTS $tableCarritoProducto (
            $idCarritoColumn INTEGER NOT NULL,
            $idProductoColumn INTEGER NOT NULL,
            PRIMARY KEY ($idCarritoColumn, $idProductoColumn),
            FOREIGN KEY ($idCarritoColumn) REFERENCES $tableCarrito($idColumn) ON DELETE CASCADE,
            FOREIGN KEY ($idProductoColumn) REFERENCES $tableProducto($idColumn) ON DELETE CASCADE
        );
    """.trimIndent()
}