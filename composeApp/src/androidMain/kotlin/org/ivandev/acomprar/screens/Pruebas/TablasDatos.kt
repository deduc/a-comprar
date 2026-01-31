package org.ivandev.acomprar.screens.Pruebas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.MainCarritoStore

class TablasDatos: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = "Tablas de datos"
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        val carritoStore: CarritoStore = viewModel()
        val mainCarritoStore: MainCarritoStore = viewModel()

        val carritos: List<CarritoEntity> = carritoStore.carritos
        var carritosAddedToMainCarrito: List<Int> = carritoStore.carritosAddedToMainCarrito

        LaunchedEffect(Dispatchers.IO) {
            carritoStore.getAllCarrito()
            mainCarritoStore.getAllCarrito()
        }

        CarritosDeLaCompra(carritos, carritosAddedToMainCarrito)
    }

    @Composable
    fun CarritosDeLaCompra(carritos: List<CarritoEntity>, carritosAddedToMainCarrito: List<Int>) {
        Text("Tabla carritos")
        LazyColumn {
            items(
                items = carritos,
                key = { it.id }
            ) { carrito ->
                Row {
                    Text("${carrito.id} - ${carrito.name} - ${carrito.description}")
                }
            }
        }

        Text("Carritos añadidos al maincarrito")
        Column() {
            carritosAddedToMainCarrito.forEach {
                Text(it.toString())
            }
        }
    }
}