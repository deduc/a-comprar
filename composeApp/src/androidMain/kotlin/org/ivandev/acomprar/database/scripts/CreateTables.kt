package org.ivandev.acomprar.database.scripts

import org.ivandev.acomprar.Literals

object CreateTables {
    // Tablas
    val tableCarrito = Literals.Database.Tables.CARRITO_TABLE
    val tableCarritoProducto = Literals.Database.Tables.CARRITO_PRODUCTO_TABLE
    val tableCategoria = Literals.Database.Tables.CATEGORIA_TABLE
    val tableComida = Literals.Database.Tables.COMIDA_TABLE
    val tableProducto = Literals.Database.Tables.PRODUCTO_TABLE
    val tableMenu = Literals.Database.Tables.MENU_TABLE
    val tableComidaProducto = Literals.Database.Tables.COMIDA_PRODUCTO_TABLE
    val tableMenuComida = Literals.Database.Tables.MENU_COMIDA_TABLE
    val tableMenuDayOfWeek = Literals.Database.Tables.MENU_DAYS_OF_WEEK
    val tableMainCarrito = Literals.Database.Tables.MAIN_CARRITO_TABLE
    val tableMainCarritoWithProducts = Literals.Database.Tables.MAIN_CARRITO_WITH_PRODUCTS
    val tableUserActions = Literals.Database.Tables.USER_ACTIONS_TABLE

    // Columnas
    val cantidadColumn = Literals.Database.ColumnNames.CANTIDAD_COLUMN
    val descriptionColumn = Literals.Database.ColumnNames.DESCRIPTION_COLUMN
    val diaColumn = Literals.Database.ColumnNames.DIA_COLUMN
    val nombreColumn = Literals.Database.ColumnNames.NOMBRE_COLUMN
    val marcaColumn = Literals.Database.ColumnNames.MARCA_COLUMN
    val idMenuColumn = Literals.Database.ColumnNames.ID_MENU_COLUMN
    val idComidaColumn = Literals.Database.ColumnNames.ID_COMIDA_COLUMN
    val idColumn = Literals.Database.ColumnNames.ID_COLUMN
    val idProductoColumn = Literals.Database.ColumnNames.ID_PRODUCTO_COLUMN
    val idCategoriaColumn = Literals.Database.ColumnNames.ID_CATEGORIA_COLUMN
    val idCarritoColumn = Literals.Database.ColumnNames.ID_CARRITO_COLUMN
    val tipoColumn = Literals.Database.ColumnNames.TIPO_COLUMN

    val isCompradoColumn = Literals.Database.ColumnNames.IS_COMPRADO_COLUMN
    val actionType = Literals.Database.ColumnNames.ACTION_TYPE_COLUMN
    val actionValue = Literals.Database.ColumnNames.ACTION_VALUE_COLUMN
    val timestamp = Literals.Database.ColumnNames.TIMESTAMP_COLUMN


    // Tablas
    val CREATE_TABLE_CARRITO = """
        CREATE TABLE IF NOT EXISTS $tableCarrito (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $nombreColumn TEXT NOT NULL,
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
            $tipoColumn BOOLEAN NOT NULL
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
            $idMenuColumn INTEGER,
            $idComidaColumn INTEGER,
            $diaColumn TEXT NOT NULL,
            $tipoColumn Text,
            FOREIGN KEY ($idMenuColumn) REFERENCES $tableMenu($idColumn) ON DELETE CASCADE,
            FOREIGN KEY ($idComidaColumn) REFERENCES $tableComida($idColumn)
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
            $cantidadColumn INTEGER NOT NULL DEFAULT 1,
            PRIMARY KEY ($idCarritoColumn, $idProductoColumn),
            FOREIGN KEY ($idCarritoColumn) REFERENCES $tableCarrito($idColumn) ON DELETE CASCADE,
            FOREIGN KEY ($idProductoColumn) REFERENCES $tableProducto($idColumn) ON DELETE CASCADE
        );
    """.trimIndent()

    val CREATE_TABLE_MAIN_CARRITO = """
        CREATE TABLE IF NOT EXISTS $tableMainCarrito (
            $idColumn INTEGER NOT NULL,
            $idCarritoColumn INTEGER NOT NULL,
            FOREIGN KEY ($idCarritoColumn) REFERENCES $tableCarrito($idColumn) ON DELETE CASCADE
        );
    """.trimIndent()

    val CREATE_TABLE_MAIN_CARRITO_WITH_PRODUCTS = """
        CREATE TABLE IF NOT EXISTS $tableMainCarritoWithProducts (
            $idColumn INTEGER NOT NULL,
            $idProductoColumn INTEGER NOT NULL,
            $cantidadColumn INTEGER DEFAULT 1,
            $isCompradoColumn INTEGER DEFAULT 0,
            FOREIGN KEY ($idProductoColumn) REFERENCES $tableProducto($idColumn) ON DELETE CASCADE
        )
    """.trimIndent()

    val CREATE_TABLE_USER_ACTIONS = """
        CREATE TABLE IF NOT EXISTS $tableUserActions (
            $idColumn INTEGER PRIMARY KEY AUTOINCREMENT,
            $actionType TEXT NOT NULL,
            $actionValue TEXT NOT NULL,
            $timestamp TEXT DEFAULT ''
        );
    """.trimIndent()
}
