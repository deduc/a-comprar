package org.ivandev.acomprar.models

class CarritoComprar {
    val carritos: List<Carrito>?
    val productos: List<Producto>?

    constructor(carritos: List<Carrito>, productos: List<Producto>) {
        this.carritos = carritos
        this.productos = productos
    }
}