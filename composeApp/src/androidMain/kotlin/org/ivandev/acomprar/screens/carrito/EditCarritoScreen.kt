package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.CategoriaStore

class EditCarritoScreen(val idCarrito: Int): Screen {
    @Composable
    override fun Content() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoName = carritoStore.carritoAndProductos.value?.carrito?.name ?: Literals.CARRITOS_TITLE

        CommonScreen(title = carritoName) { MainContent(carritoStore) }.Render()
    }

    @Composable
    fun MainContent(carritoStore: CarritoStore) {
        val carritoAndProductos: State<CarritoAndProductsData?> = carritoStore.carritoAndProductos

        LaunchedEffect(idCarrito) {
            carritoStore.getCarritoAndProductosByCarritoId(idCarrito)
        }

        Column {
            Column(Modifier.weight(1f)) {
                if (carritoAndProductos.value != null) {
                    CarritoData(carritoAndProductos.value!!, carritoStore)
                }
            }

            Row {
                ButtonsPanel()
            }
        }
    }

    @Composable
    fun ButtonsPanel() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val navigator = LocalNavigator.currentOrThrow

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { navigator.push(SeeCategoriasScreen()) }) {
                Text("Añadir productos")
            }
        }
    }

    @Composable
    fun CarritoData(carritoAndProductos: CarritoAndProductsData, carritoStore: CarritoStore) {
        val carrito: CarritoEntity = carritoAndProductos.carrito

        CarritoDescription(carrito.description)

        Spacer(Tools.spacer16dpHeight)

        CarritoProductsData(carritoAndProductos, carritoStore)
    }

    @Composable
    fun CarritoDescription(description: String) {
        Row(Modifier.border(1.dp, Color.Black).fillMaxWidth()) {
            Text(description, Modifier.border(1.dp, Color.Black).fillMaxWidth().padding(8.dp))
        }
    }

    @Composable
    fun CarritoProductsData(carritoAndProductos: CarritoAndProductsData, carritoStore: CarritoStore) {
        val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val productosAndCantidades: MutableList<Pair<ProductoEntity, Int>> = carritoAndProductos.productosAndCantidades

        Column {
            if (productosAndCantidades.isNotEmpty()) {
                Column {
                    productosAndCantidades.forEach { it: Pair<ProductoEntity, Int> ->
                        var newCantidad = carritoStore.doFixCantidadStr(it.first.cantidad, it.second)

                        Row(
                            Modifier.border(1.dp, Color.Black).padding(4.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Text(it.first.nombre, Modifier.weight(0.6f))
                                Text(newCantidad, Modifier.weight(0.4f))
                            }

                            Row {
                                MyIcons.AddIcon {  }
                                MyIcons.RemoveIcon {  }
                            }
                        }
                        Spacer(Modifier.height(Tools.height8dp))
                    }
                }
            }
            else {
                Text("Sin productos.")
            }
        }
    }
}