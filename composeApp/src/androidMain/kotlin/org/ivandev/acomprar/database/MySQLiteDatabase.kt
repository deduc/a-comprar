package org.ivandev.acomprar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.handlers.CarritoHandler
import org.ivandev.acomprar.database.handlers.CategoriaHandler
import org.ivandev.acomprar.database.handlers.ComidaHandler
import org.ivandev.acomprar.database.handlers.MenuHandler
import org.ivandev.acomprar.database.handlers.ProductoHandler
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

    fun addProducto(producto: Producto): Boolean {
        val db = writableDatabase
        val result = ProductoHandler.insert(db, producto)

        //db.close()
        return result
    }

    fun addProductosList(productos: List<Producto>): Boolean {
        val db = writableDatabase

        productos.forEach { producto ->
            if (!ProductoHandler.insert(db, producto)) {
                // Si falla uno, devolvemos false directamente
                return false
            }
        }

        return true
    }

    fun addProductoToCurrentCarrito(carrito: CarritoEntity, producto: ProductoEntity): Boolean {
        return CarritoHandler.addProductoToCurrentCarrito(writableDatabase, carrito, producto)
    }

    fun addMenuAndComidasYCenas(menu: Menu): Boolean {
        val db = writableDatabase
        val result = MenuHandler.insert(db, menu)
        //db.close()
        return result
    }

    fun addMenuDays(menuId: Int, menuDays: List<MenuDaysOfWeek>): Boolean {
        val db = writableDatabase
        val result = MenuHandler.addMenuDays(db, menuId , menuDays)
        //db.close()
        return result
    }

    fun addComida(comida: Comida): ComidaEntity? {
        val db = writableDatabase
        val result = ComidaHandler.insert(db, comida)
        //db.close()
        return result
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

    fun getLastMenu(): MenuEntity? {
        val db = readableDatabase
        val lastMenu: MenuEntity? = MenuHandler.getLast(db)

        //db.close()
        return lastMenu
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


    fun updateCategoriaById(categoriaEntity: CategoriaEntity): Boolean {
        val db = writableDatabase
        val result = CategoriaHandler.updateCategoriaById(db, categoriaEntity)

        //db.close()
        return result
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
        println("Creando tablas")
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO)
        db.execSQL(CreateTables.CREATE_TABLE_CATEGORIA)
        db.execSQL(CreateTables.CREATE_TABLE_COMIDA)
        db.execSQL(CreateTables.CREATE_TABLE_COMIDA_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_MENU)
        db.execSQL(CreateTables.CREATE_TABLE_MENU_COMIDA)
        db.execSQL(CreateTables.CREATE_TABLE_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_CARRITO_PRODUCTO)
        db.execSQL(CreateTables.CREATE_TABLE_MENU_DAY_OF_WEEK)
    }

    private fun initializeData(db: SQLiteDatabase) {
        println("Inicializando base de datos")
        CategoriaHandler.initialize(db)
        ProductoHandler.initialize(db)
    }

    fun pruebas() {
        dropTables(writableDatabase)
        setupDatabase(writableDatabase)
    }

    private fun dropTables(db: SQLiteDatabase){
        println("Borrando tablas")
        db.execSQL(DropTables.DROP_TABLE_CARRITO)
        db.execSQL(DropTables.DROP_TABLE_CATEGORIA)
        db.execSQL(DropTables.DROP_TABLE_COMIDA)
        db.execSQL(DropTables.DROP_TABLE_COMIDA_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_MENU)
        db.execSQL(DropTables.DROP_TABLE_MENU_COMIDA)
        db.execSQL(DropTables.DROP_TABLE_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_CARRITO_PRODUCTO)
        db.execSQL(DropTables.DROP_TABLE_MENU_DAYS_OF_WEEK)
    }

    private fun setupDatabase(db: SQLiteDatabase) {
        createTables(db)
        initializeData(db)
    }
}
