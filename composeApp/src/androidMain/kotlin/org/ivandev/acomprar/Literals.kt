package org.ivandev.acomprar

import org.ivandev.acomprar.database.entities.ProductoEntity

object Literals {
    const val APP_NAME: String = "A Comprar!"
    const val ADD_MENU_TITLE: String = "Añadir menú"
    const val NO_DATA_TEXT: String = "Sin datos"
    const val SIN_CANTIDAD_TEXT: String = "Sin cantidad"
    const val SIN_MARCA_TEXT: String = "Sin marca"
    const val SIN_DESCRIPCION_TEXT: String = "Descripción vacía"
    const val SIN_NOMBRE_TEXT: String = "Nombre vacío"

    // Configuration texts
    const val CHANGE_TEXT: String = "Cambiar"
    const val DELETE_ALL_DATA_TEXT: String = "Borrar todos los datos"
    const val IMPORT_JSON_TEXT: String = "Importar datos"
    const val SIZE_TEXT: String = "Tamaño del texto"

    // Home navigation buttons
    object TextHomeNavigationButtons {
        const val CATEGORIAS_TITLE: String = "Categorías"
        const val CARRITOS_TITLE: String = "Carritos de la compra"
        const val CONFIGURATION_TITLE: String = "Configuración"
        const val COMIDAS_Y_CENAS_TITLE: String = "Comidas y cenas"
        const val HOME_TITLE: String = "Home"
        const val MENU_TITLE: String = "Menús"
        const val PRODUCTOS_TITLE: String = "Productos"
        const val MI_CARRITO: String = "Mi carrito actual"

        const val USER_TABLES_TITLE: String = "Tablas de datos"
        const val EDIT_PRODUCTO_TITLE: String = "Editar producto"
        const val ESTADISTICAS_TITLE: String = "Estadísticas"
        const val PRODUCTOS_ELABORADOS_TITLE: String = "Productos elaborados"
    }

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

        object Tables {
            const val CARRITO_TABLE: String = "carrito"
            const val CARRITO_PRODUCTO_TABLE: String = "carrito_producto"
            const val CATEGORIA_TABLE: String = "categoria"
            const val COMIDA_TABLE: String = "comida"
            const val COMIDA_PRODUCTO_TABLE: String = "comida_producto"
            const val MENU_TABLE: String = "menu"
            const val MENU_COMIDA_TABLE: String = "menu_comida"
            const val MENU_DAYS_OF_WEEK: String = "menu_days_of_week"
            const val PRODUCTO_TABLE: String = "producto"
            const val MAIN_CARRITO_TABLE: String = "main_carrito"
            const val MAIN_CARRITO_WITH_PRODUCTS: String = "main_carrito_with_products"
            const val USER_ACTIONS_TABLE: String = "user_actions"
        }

        object ColumnNames {
            const val ACTION_TYPE_COLUMN: String = "action_type"
            const val ACTION_VALUE_COLUMN: String = "action_value"
            const val CANTIDAD_COLUMN: String = "cantidad"
            const val DESCRIPTION_COLUMN: String = "description"
            const val DIA_COLUMN: String = "dia"
            const val MARCA_COLUMN: String = "marca"
            const val NOMBRE_COLUMN: String = "nombre"
            const val TIPO_COLUMN: String = "tipo"
            const val UNIDAD_CANTIDAD_COLUMN: String = "unidad_cantidad"
            const val ID_COLUMN: String = "id"
            const val ID_COMIDA_COLUMN: String = "id_comida"
            const val ID_CENA_COLUMN: String = "id_cena"
            const val ID_CATEGORIA_COLUMN: String = "id_categoria"
            const val ID_CARRITO_COLUMN: String = "id_carrito"
            const val ID_PRODUCTO_COLUMN: String = "id_producto"
            const val ID_MENU_COLUMN: String = "id_menu"
            const val IS_COMPRADO_COLUMN: String = "is_comprado"
            const val TIMESTAMP_COLUMN: String = "timestamp"
        }

        object HardcodedValues {
            // special values to columns
            const val ID_SIN_CATEGORIA_VALUE: Int = 0
            const val VOID_STR: String = ""
            const val NO_CANTIDAD_PROVIDED: String = "1 ud"
            const val NO_MARCA_PROVIDED: String = ""

            // Database hardcoded Data
            const val MAIN_CARRITO_ID: Int = 1
            const val CARRITO_BASTARDO_ID: Int = 1
            const val CARRITO_BASTARDO_NAME: String = "carrito_bastardo"
            const val CARRITO_BASTARDO_DESCRIPTION: String = "Carrito auxiliar que no deberían ver los usuarios."
            const val CARRITO_ESPECIAL_NAME: String = "Falta por comprar"
            const val CARRITO_ESPECIAL_DESCRIPTION: String = "Productos que no llegaste a comprar en la fecha"

        }

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

    object ScreenTitles {
        fun editComidaTitle(nombreComida: String): String {
            return "Editar $nombreComida"
        }
    }

    object ToastText {
        const val ADDED_DATA_TEST = "Datos de prueba añadidos."
        const val ADDED_MENU = "Menú añadido."
        const val ADDED_PRODUCTO = "Producto añadido."
        const val ADDED_CARRITO_TO_MAIN_CARRITO = "Carrito añadido para comprar."
        const val ADDED_CATEGORIA = "Categoría añadida."

        const val STOPPED_BUYING_OK_TITLE = "Compra detenida con éxito."
        const val STOPPED_BUYING_OK_TEXT = "Los productos que no hubieras comprado se han añadido a un nuevo carrito."

        const val DELETED_ALL_PRODUCTOS = "Productos borrados."

        const val DELETED_CARRITO_FROM_MAIN_CARRITO = "Carrito borrado de la lista"
        const val DELETING_CARRITO_FROM_MAIN_CARRITO = "¿Seguro que quieres quitar este carrito?"

        const val ERROR_ADDING_DATA_TEST = "Error al añadir registros de prueba."
        const val ERROR_ADDING_PRODUCTO = "Error al manejar el producto."
        const val ERROR_ADDING_MENU = "Error al añadir el menú."
        const val ERROR_ADDING_MENU_DAYS = "Error al añadir los días del menú."
        const val ERROR_MANAGING_PRODUCTO = "Error inesperado al sumar o restar producto."
        const val ERROR_VOID_DATA = "Error no has rellenado suficientes datos."
        const val ERROR_ADDING_PRODUCTO_TO_UNKNOWN_CARRITO = "Error añadiendo el producto a un carrito desconocido"
        const val ERROR_DELETING_ALL_PRODUCTOS = "Error al añadir registros de prueba."
        const val ERROR_DELETING_CARRITO_FROM_MAIN_CARRITO = "Error al borrar el carrito de la lista"
        const val ERROR_NO_NAME_PROVIDED = "ERROR: El campo Nombre no puede estar vacío."
        const val ERROR_ADDING_CARRITO_TO_MAIN_CARRITO = "ERROR añadiendo carrito para comprar"
        const val ERROR_NULL_ID_CATEGORIA = "ERROR INTERNO: No hay id categoria"
        const val ERROR_PRODUCTO_NAME = "ERROR: No hay nombre de producto"
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

    object TextDialog {
        const val A_COMPRAR_CONFIRMATION: String = "¿Seguro que quieres comenzar a comprar?"
        const val STOP_BUYING_CONFIRMATION: String = "¿Seguro que quieres parar de comprar?"
    }

    object UserActions {
        const val USER_BUYING: String = "user_buying"
    }
}
