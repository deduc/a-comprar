package org.ivandev.acomprar.database.interfaces

interface DatabaseMethods {
    fun importJsonData()
    fun deleteAll()
    fun deleteAllCarrito()
    fun deleteAllCategoria()
    fun deleteAllMenu()
    fun deleteAllProducto()

}