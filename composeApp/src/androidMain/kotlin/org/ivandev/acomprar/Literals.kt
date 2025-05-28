package org.ivandev.acomprar

object Literals {
    const val APP_NAME: String = "A Comprar!"
    const val ADD_MENU_TITLE: String = "Añadir menú"
    const val NO_DATA_TEXT: String = "Sin datos"
    const val SIN_CANTIDAD_TEXT: String = "Sin cantidad"
    const val SIN_MARCA_TEXT: String = "Sin marca"

    // Configuration texts
    const val CHANGE_TEXT: String = "Cambiar"
    const val DELETE_ALL_DATA_TEXT: String = "Borrar todos los datos"
    const val IMPORT_JSON_TEXT: String = "Importar datos"
    const val SIZE_TEXT: String = "Tamaño del texto"

    // Home navigation buttons
    const val CATEGORIAS_TITLE: String = "Categorías"
    const val CARRITOS_TITLE: String = "Carritos de la compra"
    const val CONFIGURATION_TITLE: String = "Configuración"
    const val COMIDAS_Y_CENAS_TITLE: String = "Comidas y cenas"
    const val EDIT_PRODUCTO_TITLE: String = "Editar producto"
    const val ESTADISTICAS_TITLE: String = "Estadísticas"
    const val PRODUCTOS_ELABORADOS_TITLE: String = "Productos elaborados"
    const val HOME_TITLE: String = "Home"
    const val MENU_TITLE: String = "Menús"
    const val PRODUCTOS_TITLE: String = "Productos"

    object ButtonsText {
        const val ADD_CATEGORIA: String = "Añadir categoría"
        const val ADD_PRODUCTO: String = "Añadir producto"
        const val ADD_MENU: String = "Añadir menú"
        const val ADD_TEST_DATA: String = "Añadir datos de prueba"
        const val CANEL_ACTION: String = "Cancelar"
        const val DELETE_ALL_PRODUCTS: String = "Borrar todos los productos"
        const val INITIALIZE_APP: String = "Restablecer datos de fábrica"
    }

    object ConfirmationText {
        fun BORRAR_MENU(menuName: String): String {
            return "¿Confirmas que vas a borrar el menú '$menuName'?"
        }
    }

    object DaysOfWeek {
        const val LUNES: String = "Lunes"
        const val MARTES: String = "Martes"
        const val MIERCOLES: String = "Miércoles"
        const val JUEVES: String = "Jueves"
        const val VIERNES: String = "Viernes"
        const val SABADO: String = "Sábado"

        const val DOMINGO: String = "Domingo"
        fun getDaysOfWeek(): List<String> {
            return listOf(LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO)
        }
    }

    object Database {
        // DATABASE
        // Base directory for app A Comprar
        const val DATABASE_NAME: String = "acomprar.db"

        // Database table names
        const val CARRITO_TABLE: String = "carrito"
        const val CARRITO_PRODUCTO_TABLE: String = "carrito_producto"
        const val CATEGORIA_TABLE: String = "categoria"
        const val COMIDA_TABLE: String = "comida"
        const val COMIDA_PRODUCTO_TABLE: String = "comida_producto"
        const val MENU_TABLE: String = "menu"
        const val MENU_COMIDA_TABLE: String = "menu_comida"
        const val MENU_DAYS_OF_WEEK: String = "menu_days_of_week"
        const val PRODUCTO_TABLE: String = "producto"

        // Database column names
        const val ID_COLUMN: String = "id"
        const val ID_COMIDA_COLUMN: String = "id_comida"
        const val ID_CENA_COLUMN: String = "id_cena"
        const val ID_CATEGORIA_COLUMN: String = "id_categoria"
        const val ID_CARRITO_COLUMN: String = "id_carrito"
        const val ID_PRODUCTO_COLUMN: String = "id_producto"
        const val ID_MENU_COLUMN: String = "id_menu"
        const val CANTIDAD_COLUMN: String = "cantidad"
        const val DESCRIPTION_COLUMN: String = "description"
        const val DIA_COLUMN: String = "dia"
        const val UNIDAD_CANTIDAD_COLUMN: String = "unidad_cantidad"
        const val MARCA_COLUMN: String = "marca"
        const val NOMBRE_COLUMN: String = "nombre"
        const val TIPO_COLUMN: String = "tipo"

        // special values to columns
        const val ID_SIN_CATEGORIA_VALUE: Int = 0
        const val VOID_STR: String = ""

        object Categorias {
            const val SIN_CATEGORIA: String = "Sin categoría"
            const val CARNE: String = "Carne"
            const val PASTA: String = "Pasta"
            const val LACTEOS: String = "Lácteos"
            const val FRUTA: String = "Fruta"
            const val VERDURA: String = "Verdura"
            const val FRIGORIFICOS: String = "Frigoríficos"
            const val CONGELADOS: String = "Congelados"
            const val LIMPIEZA: String = "Limpieza"
            const val COMIDAS: String = "Comidas"
            const val CENAS: String = "Cenas"
            const val POSTRES: String = "Postres"

            fun getDefaultCategorias(): List<String> {
                return listOf(SIN_CATEGORIA, CARNE, PASTA, LACTEOS, FRUTA, VERDURA, FRIGORIFICOS, CONGELADOS, LIMPIEZA, COMIDAS, CENAS, POSTRES)
            }
        }
    }

    object Errors {
        const val NO_NAME_PROVIDED: String = "ERROR: El campo Nombre no puede estar vacío."
    }

    object ScreenTitles {
        fun editComidaTitle(nombreComida: String): String {
            return "Editar $nombreComida"
        }
    }

    object ToastText {
        const val ADDED_DATA_TEST = "Datos de prueba añadidos."
        const val ADDED_MENU = "Menú añadido."

        const val DELETED_ALL_PRODUCTOS = "Productos borrados."

        const val ERROR_ADDING_DATA_TEST = "Error al añadir registros de prueba."
        const val ERROR_ADDING_MENU = "Error al añadir el menú."
        const val ERROR_ADDING_MENU_DAYS = "Error al añadir los días del menú."
        const val ERROR_VOID_DATA = "Error no has rellenado suficientes datos."

        const val ERROR_DELETING_ALL_PRODUCTOS = "Error al añadir registros de prueba."
    }

    // No SQL tables, just for UI "tables"
    object UITables {
        const val NOMBRE_COLUMN: String = "Nombre"
        const val CANTIDAD_COLUMN: String = "Cantidad"
        const val OPCIONES_COLUMN: String = "Opciones"
        const val MARCA_COLUMN: String = "Marca"
        const val DIA_COLUMN: String = "Día"
        const val COMIDA_COLUMN: String = "Comida"
        const val CENA_COLUMN: String = "Cena"

        fun getComidasYCenasTableHeaders(): List<String> {
            return listOf(DIA_COLUMN, COMIDA_COLUMN, CENA_COLUMN)
        }
    }
}
