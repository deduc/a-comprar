package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CarritoProductoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.stores.CarritoStore

class EditCarritoScreen(val idCarrito: Int): Screen {
    @Composable
    override fun Content() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoName = carritoStore.carritoAndProductos.value?.carrito?.name ?: Literals.CARRITOS_TITLE

        CommonScreen(title = carritoName) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoAndProductos: State<CarritoAndProductsData?> = carritoStore.carritoAndProductos

        LaunchedEffect(idCarrito) {
            carritoStore.getCarritoAndProductosByCarritoId(idCarrito)
        }

        if (carritoAndProductos.value != null) {
            CarritoData(carritoAndProductos.value!!)
        }

        ButtonsPanel()
    }

    @Composable
    fun ButtonsPanel() {
//        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val navigator = LocalNavigator.currentOrThrow

        Button(onClick = { navigator.push(SeeCategoriasScreen()) }) {
            Text("Añadir productos")
        }

    }

    @Composable
    fun CarritoData(carritoAndProductos: CarritoAndProductsData) {
        val carrito: CarritoEntity = carritoAndProductos.carrito
        val productosAndCantidades: List<Pair<ProductoEntity, Int>> = carritoAndProductos.productosAndCantidades

        Column {
            Text("${carrito.id} - ${carrito.name}")
            Text(carrito.description)
        }

        Spacer(Tools.spacer16dpHeight)

        Column {
            if (productosAndCantidades.isNotEmpty()) {
                productosAndCantidades.forEach {
                    Row {
                        Text("${it.first.nombre} - ${it.second}")
                    }
                }
            }
            else {
                Text("Sin productos.")
            }
        }
    }
}