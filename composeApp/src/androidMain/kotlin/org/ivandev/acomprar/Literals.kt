package org.ivandev.acomprar

object Literals {
    const val APP_NAME: String = "A Comprar!"

    const val CHANGE_TEXT: String = "Cambiar"
    const val DELETE_ALL_DATA_TEXT: String = "Borrar todos los datos."
    const val IMPORT_JSON_TEXT: String = "Importar datos."
    const val SIZE_TEXT: String = "Tamaño del texto."

    const val CONFIGURATION_SCREEN_TITLE: String = "Configuración"

    // HomeNavigationButtons
    const val HOME_TITLE: String = "Home"
    const val MENU_TITLE: String = "Menús"
    const val PRODUCTOS_TITLE: String = "Productos"
    const val CATEGORIAS_TITLE: String = "Categorías"
    const val ESTADISTICAS_TITLE: String = "Estadísticas"
    const val COMIDAS_Y_CENAS_TITLE: String = "Comidas y cenas"
    const val CARRITOS_TITLE: String = "Carritos de la compra"
    const val PRODUCTOS_ELABORADOS_TITLE: String = "Productos elaborados"

    object Database {
        // DATABASE
        // Base directory for app A Comprar
        const val INTERNAL_DIRECTORY: String = "Database"
        const val DATABASE_FOLDER: String = INTERNAL_DIRECTORY
        const val DATABASE_NAME: String = "acomprar.db"

        // Database table names
        const val CARRITO_TABLE: String = "carrito"
        const val CATEGORIA_TABLE: String = "categoria"
        const val MENU_TABLE: String = "menu"
        const val PRODUCTO_TABLE: String = "producto"
        const val CARRITO_PRODUCTO_TABLE: String = "carrito_producto"

        // Database column names
        const val ID_COLUMN: String = "id"
        const val DESCRIPTION_COLUMN: String = "description"
        const val NOMBRE_COLUMN: String = "nombre"
        const val ID_CATEGORIA_COLUMN: String = "id_categoria"
        const val CANTIDAD_COLUMN: String = "cantidad"
        const val UNIDAD_CANTIDAD_COLUMN: String = "unidad_cantidad"
        const val MARCA_COLUMN: String = "marca"
        const val ID_CARRITO_COLUMN: String = "id_carrito"
        const val ID_PRODUCTO_COLUMN: String = "id_producto"


        // File names
        const val CARRITO_FILE: String = "carrito.json"
        const val CATEGORIA_FILE: String = "categoria.json"
        const val MENU_FILE: String = "menu.json"
        const val PRODUCTO_FILE: String = "producto.json"
    }
}
