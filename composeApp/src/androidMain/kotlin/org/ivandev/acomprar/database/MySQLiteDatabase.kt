package org.ivandev.acomprar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.Database.mySQLiteDatabase
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.database.handlers.CarritoHandler
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.ComidaHandler
import org.ivandev.acomprar.database.handlers.MainCarritoHandler
import org.ivandev.acomprar.database.handlers.MenuHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
import org.ivandev.acomprar.database.handlers.UserActionsHandler
import org.ivandev.acomprar.database.scripts.CreateTables
import org.ivandev.acomprar.database.scripts.DropTables
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Categoria
import org.ivandev.acomprar.models.Comida
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.models.Producto

class MySQLiteDatabase(context: Context, version: Int) : SQLiteOpenHelper(
    context,
    Literals.Database.DATABASE_NAME,
    null,
    version
)
{
    // Se ejecuta este método la primera vez que usas
    // las lineas db.writableDatabase o db.readableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { it: SQLiteDatabase ->
            setupDatabase(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            dropTables(it)
            setupDatabase(it)
        }
    }

    fun addCarrito(carrito: Carrito): Boolean {
        val db = writableDatabase
        return CarritoHandler.add(db, carrito)
    }

    fun addCategoria(categoria: Categoria): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.insert(db, categoria)

        //db.close()
        return result
    }

    fun addCarritoToMainCarrito(id: Int): Boolean {
        return MainCarritoHandler.addCarritoToMainCarrito(writableDatabase, id)
    }

    fun addProducto(producto: Producto): Boolean {
        return ProductoHandler.add(writableDatabase, producto)
    }

    fun addProductosList(productos: List<Producto>): Boolean {
        return ProductoHandler.addProductosList(writableDatabase, productos)
    }

    fun addProductoToCurrentCarrito(carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        return CarritoHandler.addProductoToCurrentCarrito(writableDatabase, carrito, producto)
    }

    fun substractProductoToCurrentCarrito(carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        return CarritoHandler.substractProductoToCurrentCarrito(writableDatabase, carrito, producto)
    }

    fun setUserIsBuying(value: String) {
        UserActionsHandler.setUserIsBuying(writableDatabase, value)
    }



    fun addMenuAndComidasYCenas(menu: Menu): Boolean {
        return MenuHandler.insert(writableDatabase, menu)
    }

    fun addMenuDays(menuId: Int, menuDays: List<MenuDaysOfWeek>): Boolean {
        return MenuHandler.addMenuDays(writableDatabase, menuId , menuDays)
    }

    fun addComida(comida: Comida): ComidaEntity? {
        val db = writableDatabase
        val result = ComidaHandler.insert(db, comida)
        //db.close()
        return result
    }

    fun checkIfCarritosWasAddedToMainCarrito(idCarritos: List<Int>): List<Int> {
        return CarritoHandler.checkIfCarritosWasAddedToMainCarrito(readableDatabase, idCarritos)
    }
    fun checkIfCarritoExists(carrito: Carrito): Boolean {
        return CarritoHandler.checkIfCarritoExists(readableDatabase, carrito)
    }



    fun getAllCategoria(): List<CategoriaEntity> {
        val db = readableDatabase
        val result = CategoriaHandler.getAll(db)

        //db.close()
        return result
    }

    fun getAllComidas(): MutableList<ComidaEntity> {
        val db = readableDatabase
        val result = ComidaHandler.getAll(db)
        //db.close()
        return result
    }


    fun getAllProductosByCategoria(): List<CategoriaWithProductos> {
        val db = readableDatabase

        val categoriaEntities: List<CategoriaEntity> = CategoriaHandler.getAll(db)
        val result = ProductoHandler.getAllProductosByCategoria(db, categoriaEntities)

        //db.close()
        return result
    }

    fun getAllMenu(): List<MenuEntity> {
        val db = readableDatabase
        val result = MenuHandler.getAll(db)

        //db.close()
        return result
    }

    fun getCategoriaIdByProductoId(producto: Producto): Int {
        return ProductoHandler.getCategoriaIdByProductoId(readableDatabase, producto)
    }

    fun getProductoById(id: Int): ProductoEntity? {
        return ProductoHandler.getById(readableDatabase, id)
    }

    fun getProductosByCategoriaId(id: Int): List<ProductoEntity> {
        val db = readableDatabase
        val result = ProductoHandler.getProductosByCategoriaId(db, id)

        //db.close()
        return result
    }

    fun getComidasByTipoId(id: Int): List<ComidaEntity> {
        val db = readableDatabase
        val result = ComidaHandler.getComidasByTipoId(db, id)

        //db.close()
        return result
    }

    fun getCarritoAndProductosByCarritoId(id: Int): CarritoAndProductsData {
        return CarritoHandler.getCarritoAndProductosByCarritoId(readableDatabase, id)
    }
    fun getCarritoById(id: Int): CarritoEntity {
        return CarritoHandler.getCarritoById(readableDatabase, id)
    }
    fun getCarritosAddedToMainCarrito(): List<CarritoEntity> {
        return MainCarritoHandler.getCarritosAddedToMainCarrito(readableDatabase)
    }

    fun getLastMenu(): MenuEntity? {
        val db = readableDatabase
        val result: MenuEntity? = MenuHandler.getLast(db)

        //db.close()
        return result
    }

    fun getAllCarrito(): List<CarritoEntity> {
        val db = readableDatabase
        val carritos: List<CarritoEntity> = CarritoHandler.getAll(db)

        return carritos
    }

    fun getMenuDaysOfWeekByMenuId(menuId: Int): MutableList<MenuDaysOfWeekEntity> {
        val db = readableDatabase
        val result = MenuHandler.getMenuDaysOfWeekByMenuId(db, menuId)

        //db.close()
        return result
    }

    fun getUserActionByActionType(actionType: String): UserActionsEntity {
        return UserActionsHandler.getUserActionByActionType(readableDatabase, actionType)
    }

    fun knowIfUserIsBuying(): Boolean {
        return UserActionsHandler.knowIfUserIsBuying(readableDatabase)
    }

    fun updateCategoriaById(categoriaEntity: CategoriaEntity): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.updateCategoriaById(db, categoriaEntity)

        //db.close()
        return result
    }
    fun updateCarrito(carrito: Carrito): Boolean {
        return CarritoHandler.updateCarrito(writableDatabase, carrito)
    }


    fun updateComidaById(comida: ComidaEntity): Boolean {
        return ComidaHandler.updateById(writableDatabase, comida)
    }

    fun updateProductoById(productoEntity: ProductoEntity): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.updateById(db, productoEntity)

        //db.close()
        return result
    }

    fun updateProductosToSinCategoria(idCategoria: Int): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.updateProductosToSinCategoria(db, idCategoria)

        //db.close()
        return result
    }

    fun updateMenuNameById(menu: MenuEntity): Boolean {
        val db = writableDatabase
        val result = MenuHandler.updateMenuNameById(db, menu)

        //db.close()
        return result
    }

    fun updateMenuDaysOfWeekById(menuDaysOfWeek: MenuDaysOfWeek): Boolean {
        return MenuHandler.updateMenuDaysOfWeekById(writableDatabase, menuDaysOfWeek)
    }

    fun deleteAllProducto(): Boolean {
        val db = writableDatabase
        val productos: List<ProductoEntity> = ProductoHandler.getAll(db)

        productos.forEach {
            println("Borrando producto con id = ${it.id}")
            ProductoHandler.deleteById(db, it.id)
        }

        val result: List<ProductoEntity> = ProductoHandler.getAll(db)

        //db.close()
        return result.size == 0
    }

    fun deleteCarritoFromMainCarrito(id: Int): Boolean {
        return MainCarritoHandler.deleteCarritoFromMainCarrito(writableDatabase, id)
    }

    fun deleteCategoriaById(id: Int): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.deleteById(db, id)
        //db.close()
        return result
    }

    fun deleteCarritoById(id: Int): Boolean {
        return CarritoHandler.deleteById(writableDatabase, id)
    }

    fun deleteComidaById(comidaId: Int): Boolean {
        val db = writableDatabase
        val result = ComidaHandler.deleteById(db, comidaId)

        //db.close()
        return result
    }

    fun deleteProductoById(id: Int): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.deleteById(db, id)

        //db.close()
        return result
    }


    fun deleteMenu(menuEntity: MenuEntity): Boolean {
        val db = writableDatabase
        val deletedMenu = MenuHandler.delete(db, menuEntity)
        var result = false

        if (deletedMenu) {
            result = ComidaHandler.deleteAllComidasByMenuId(db, menuEntity.id!!)
        }

        //db.close()
        return result
    }

    fun deleteLastMenu(): Boolean {
        val db = writableDatabase
        val result = MenuHandler.deleteLast(db)

        //db.close()
        return result
    }

    // METODOS ESPECIALES
    // METODOS ESPECIALES
    // METODOS ESPECIALES
    private fun createTables(db: SQLiteDatabase) {
        println("[DEBUG] Creando tablas")
        val createStatements = listOf(
            CreateTables.CREATE_TABLE_CARRITO,
            CreateTables.CREATE_TABLE_CATEGORIA,
            CreateTables.CREATE_TABLE_COMIDA,
            CreateTables.CREATE_TABLE_PRODUCTO,
            CreateTables.CREATE_TABLE_COMIDA_PRODUCTO,
            CreateTables.CREATE_TABLE_MENU,
            CreateTables.CREATE_TABLE_MENU_COMIDA,
            CreateTables.CREATE_TABLE_MENU_DAY_OF_WEEK,
            CreateTables.CREATE_TABLE_CARRITO_PRODUCTO,
            CreateTables.CREATE_TABLE_MAIN_CARRITO,
            CreateTables.CREATE_TABLE_MAIN_CARRITO_WITH_PRODUCTS,
            CreateTables.CREATE_TABLE_USER_ACTIONS,
        )

        // Ejecutar cada una de las sentencias
        createStatements.forEach { statement ->
            db.execSQL(statement)
            println("[DEBUG] Tabla creada")
        }
    }

    private fun initializeData(db: SQLiteDatabase) {
        println("[DEBUG] Inicializando base de datos")

        CategoriaHandler.initialize(db)
        ProductoHandler.initialize(db)
        CarritoHandler.initialize(db)
        MainCarritoHandler.initialize(db)
        UserActionsHandler.initialize(db)
    }

    fun restartDatabase() {
        dropTables(writableDatabase)
        setupDatabase(writableDatabase)
    }

    private fun dropTables(db: SQLiteDatabase) {
        println("[DEBUG] Borrando tablas")

        // El orden de borrado es importante. Se borran primero las tablas
        // que dependen de otras (las que tienen FOREIGN KEY).
        val dropStatements = listOf(
            DropTables.DROP_TABLE_CARRITO_PRODUCTO,
            DropTables.DROP_TABLE_MENU_DAYS_OF_WEEK,
            DropTables.DROP_TABLE_MENU_COMIDA,
            DropTables.DROP_TABLE_COMIDA_PRODUCTO,
            DropTables.DROP_TABLE_PRODUCTO,
            DropTables.DROP_TABLE_MENU,
            DropTables.DROP_TABLE_COMIDA,
            DropTables.DROP_TABLE_CATEGORIA,
            DropTables.DROP_TABLE_CARRITO,
            DropTables.DROP_TABLE_MAIN_CARRITO,
            DropTables.DROP_TABLE_MAIN_CARRITO_WITH_PRODUCTS,
            DropTables.DROP_TABLE_USER_ACTIONS,
        )

        // Ejecutar cada una de las sentencias
        dropStatements.forEach { statement ->
            // Usamos try-catch por si la tabla no existiera, para evitar que la app crashee
            try {
                db.execSQL(statement)
                println("[DEBUG] Tabla borrada")
            } catch (e: Exception) {
                // Opcional: registrar el error si es necesario
                 println("[Error] Error al borrar tabla: ${e.message}")
            }
        }
    }

    private fun setupDatabase(db: SQLiteDatabase) {
        createTables(db)
        initializeData(db)
    }
}
