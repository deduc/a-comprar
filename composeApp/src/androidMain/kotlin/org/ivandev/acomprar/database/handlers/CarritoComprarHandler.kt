package org.ivandev.acomprar.database.handlers

import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.special_classes.CarritoComprarEntity
import org.ivandev.acomprar.models.Carrito
import org.ivandev.acomprar.models.Producto


object CarritoComprarHandler {

    fun create(carritoComprar: CarritoComprarEntity): Boolean {
        var success = true
        carritoComprar.carritos.forEach { carrito ->
            if (!Database.addCarrito(carrito)) {
                success = false
            }
        }
        carritoComprar.productos.forEach { producto ->
            if (!Database.addProducto(producto)) {
                success = false
            }
        }
        return success
    }

    fun getById(id: Int): CarritoComprarEntity {
        val carritoAndProductos = Database.getCarritoAndProductosByCarritoId(id)
        val carrito = Carrito(
            id = carritoAndProductos.carrito.id,
            name = carritoAndProductos.carrito.name,
            description = carritoAndProductos.carrito.description
        )
        val productos = carritoAndProductos.productosAndCantidades.map { (productoEntity, _) ->
            Producto(
                id = productoEntity.id,
                nombre = productoEntity.nombre,
                cantidad = productoEntity.cantidad,
                idCategoria = productoEntity.idCategoria,
                marca = productoEntity.marca
            )
        }
        return CarritoComprarEntity().apply {
            this.carritos = listOf(carrito)
            this.productos = productos
        }
    }

    fun getAll(): CarritoComprarEntity {
        val carritos = Database.getAllCarrito().map { carritoEntity ->
            Carrito(
                id = carritoEntity.id,
                name = carritoEntity.name,
                description = carritoEntity.description
            )
        }
        val productos = Database.getAllProducto().map { productoEntity ->
            Producto(
                id = productoEntity.id,
                nombre = productoEntity.nombre,
                cantidad = productoEntity.cantidad,
                idCategoria = productoEntity.idCategoria,
                marca = productoEntity.marca
            )
        }
        return CarritoComprarEntity().apply {
            this.carritos = carritos
            this.productos = productos
        }
    }

    fun updateById(id: Int, carrito: Carrito): Boolean {
        // La base de datos no tiene un método para actualizar, así que borramos y creamos
        return if (Database.deleteCarritoById(id)) {
            Database.addCarrito(carrito)
        } else {
            false
        }
    }

    fun deleteById(id: Int): Boolean {
        return Database.deleteCarritoById(id)
    }
}