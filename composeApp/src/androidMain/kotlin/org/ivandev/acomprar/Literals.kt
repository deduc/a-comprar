package org.ivandev.acomprar

import org.ivandev.acomprar.database.entities.ProductoEntity

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
    const val USER_TABLES_TITLE: String = "Tablas de datos"
    const val COMIDAS_Y_CENAS_TITLE: String = "Comidas y cenas"
    const val EDIT_PRODUCTO_TITLE: String = "Editar producto"
    const val ESTADISTICAS_TITLE: String = "Estadísticas"
    const val PRODUCTOS_ELABORADOS_TITLE: String = "Productos elaborados"
    const val HOME_TITLE: String = "Home"
    const val MENU_TITLE: String = "Menús"
    const val PRODUCTOS_TITLE: String = "Productos"

    object ButtonsText {
        const val ADD_CATEGORIA: String = "Añadir categoría"
        const val ADD_CARRITO: String = "Añadir carrito"
        const val ADD_COMIDA: String = "Añadir comida"
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

        val USER_TABLES: List<String> = listOf(
            CARRITO_TABLE, CARRITO_PRODUCTO_TABLE, CATEGORIA_TABLE, COMIDA_TABLE, COMIDA_PRODUCTO_TABLE, MENU_TABLE, MENU_COMIDA_TABLE, MENU_DAYS_OF_WEEK, PRODUCTO_TABLE
        )

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
            const val POSTRES: String = "Postres"
            const val PANADERIA_BOLLERIA: String = "Panadería y bollería"
            const val PESCADO: String = "Pescado"
            const val FRUTOS_SECOS: String = "Frutos secos"
            const val DESAYUNOS: String = "Desayunos"

            fun getDefaultCategorias(): List<String> {
                return listOf(
                    SIN_CATEGORIA, CARNE, PASTA, LACTEOS, FRUTA, VERDURA,
                    PANADERIA_BOLLERIA, FRIGORIFICOS, CONGELADOS, LIMPIEZA,
                    POSTRES, PESCADO, FRUTOS_SECOS, DESAYUNOS
                )
            }
        }

        object Productos {
            val LISTA_PRODUCTOS_BRUTO: List<String> = listOf(
                "queso lonchas", "bacon lonchas", "pan burger x1", "mini taquitos jamon", "jamon lonchas",
                "taco jamon", "macarrones", "naranjas", "platanos", "pilas aa", "botes fanta limon",
                "botes cocacola", "vino", "calabacin x2", "cuerda de atar carnes", "cervezas", "anchoas",
                "masa empanadillas x2", "judias verdes bote", "patatas", "huevos", "pimientos verdes x5",
                "pimiento rojo", "cebollas", "ajos", "cuña queso semi curado", "fuet", "pimientos padron dulces",
                "jamon", "limon x1", "zanahorias", "tomates x3", "papel horno", "pañuelos", "pan bimbo",
                "pan burger x2", "croisanes x2", "salsa barbacoa", "atun en aceite (no marca didi)",
                "placas de lasaña verde (directa al horno)", "barra de pan x2", "salchichas", "bacon",
                "jamon de york", "4 quesos rayado", "brocoli", "puerro", "desodorante papa", "cereales oscar",
                "papel film transparente", "alcachofas bote x2", "pimientos del piquillo en tiras", "harina",
                "soja", "leche", "nueces", "natillas", "cafe descafeinado", "calabacin x3", "queso feta",
                "servilletas", "leche de soja", "caldo de pollo", "pasta pajaritas de colores",
                "tomate triturado natural", "guantes de latex", "colonia 43", "azucar", "tomate frito",
                "croisanes rellenos x2", "queso en lonchas x2", "lechuga"
            )

            val PALABRAS_CLAVE_PRODUCTO_CATEGORIA: Map<String, String> = mapOf(
                "azucar" to "Sin categoría",
                "barbacoa" to "Sin categoría",
                "caldo" to "Sin categoría",
                "harina" to "Sin categoría",
                "tomate triturado" to "Sin categoría",
                "tomate frito" to "Sin categoría",
                "masa empanadillas" to "Sin categoría",

                "jamon" to "Carne",
                "bacon" to "Carne",
                "salchicha" to "Carne",
                "fuet" to "Carne",
                "pollo" to "Carne",

                "macarrones" to "Pasta",
                "pasta" to "Pasta",
                "lasaña" to "Pasta",

                "queso" to "Lácteos",
                "leche" to "Lácteos",
                "soja" to "Lácteos",
                "yogur" to "Lácteos",
                "huevos" to "Lácteos",
                "queso feta" to "Lácteos",
                "queso en lonchas" to "Lácteos",
                "queso lonchas" to "Lácteos",
                "cuña queso semi curado" to "Lácteos",
                "4 quesos rayado" to "Lácteos",

                "naranja" to "Fruta",
                "platano" to "Fruta",
                "limon" to "Fruta",
                "manzana" to "Fruta",

                "lechuga" to "Verdura",
                "tomate" to "Verdura",
                "zanahoria" to "Verdura",
                "calabacin" to "Verdura",
                "cebolla" to "Verdura",
                "pimiento" to "Verdura",
                "brocoli" to "Verdura",
                "puerro" to "Verdura",
                "judia" to "Verdura",
                "alcachofa" to "Verdura",
                "piquillo" to "Verdura",
                "patata" to "Verdura",
                "ajo" to "Verdura",

                "pan" to "Panadería y bollería",
                "croisanes" to "Panadería y bollería",
                "bollería" to "Panadería y bollería",
                "bollo" to "Panadería y bollería",
                "pan bimbo" to "Panadería y bollería",
                "barra de pan" to "Panadería y bollería",
                "pan burger" to "Panadería y bollería",
                "croisanes rellenos" to "Panadería y bollería",

                "fanta" to "Frigoríficos",
                "cocacola" to "Frigoríficos",
                "cerveza" to "Frigoríficos",
                "vino" to "Frigoríficos",

                "anchoas" to "Pescado",
                "atun" to "Pescado",

                "congelado" to "Congelados",

                "desodorante" to "Limpieza",
                "pañuelo" to "Limpieza",
                "servilleta" to "Limpieza",
                "guantes" to "Limpieza",
                "papel" to "Limpieza",
                "film" to "Limpieza",
                "cuerda" to "Limpieza",
                "colonia" to "Limpieza",

                "nuez" to "Frutos secos",
                "nueces" to "Frutos secos",

                "cereal" to "Desayunos",
                "cereales" to "Desayunos",
                "cafe" to "Desayunos",
                "cafe descafeinado" to "Desayunos",

                "natilla" to "Postres"
            )

            fun doBuildProductoEntityList(lista: List<String>): List<ProductoEntity> {
                val categorias = Categorias.getDefaultCategorias()

                return lista.map { item ->
                    val regex = Regex("^(.*?)(?:\\s+x?(\\d+))?$")
                    val match = regex.find(item.trim())

                    val nombreLimpio = match?.groupValues?.get(1)?.trim()?.lowercase() ?: item.lowercase()
                    val cantidadExtraida = match?.groupValues?.get(2)?.takeIf { it.isNotEmpty() }?.let { "$it ud" } ?: "1 ud"

                    // Buscar la categoría por palabra clave
                    val categoria = PALABRAS_CLAVE_PRODUCTO_CATEGORIA.entries.firstOrNull { (clave, _) -> nombreLimpio.contains(clave) }?.value ?: Categorias.SIN_CATEGORIA

                    val idCategoria = categorias.indexOfFirst { it.equals(categoria, ignoreCase = true) }
                        .takeIf { it >= 0 } ?: 0

                    ProductoEntity(
                        id = 0,
                        idCategoria = idCategoria,
                        nombre = nombreLimpio.replaceFirstChar { it.uppercase() },
                        cantidad = cantidadExtraida,
                        marca = null
                    )
                }
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
        const val ADDED_PRODUCTO = "Producto añadido."

        const val REMOVED_PRODUCTO = "Producto restado."

        const val DELETED_ALL_PRODUCTOS = "Productos borrados."

        const val ERROR_ADDING_DATA_TEST = "Error al añadir registros de prueba."
        const val ERROR_ADDING_PRODUCTO = "Error al manejar el producto."
        const val ERROR_ADDING_MENU = "Error al añadir el menú."
        const val ERROR_ADDING_MENU_DAYS = "Error al añadir los días del menú."
        const val ERROR_MANAGING_PRODUCTO = "Error inesperado al sumar o restar producto."
        const val ERROR_VOID_DATA = "Error no has rellenado suficientes datos."
        const val ERROR_ADDING_PRODUCTO_TO_UNKNOWN_CARRITO = "Error añadiendo el producto a un carrito desconocido"

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
