package org.ivandev.acomprar.stores

import androidx.lifecycle.ViewModel
import org.ivandev.acomprar.database.Database

class CarritoStore: ViewModel() {
    fun getAllCarrito() {
        Database.getAllCarrito()
    }
}