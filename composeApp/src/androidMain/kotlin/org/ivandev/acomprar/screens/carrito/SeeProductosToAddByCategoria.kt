package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.screens.producto.AddProductoPopup
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.CategoriaStore
import org.ivandev.acomprar.stores.ProductoStore

class SeeProductosToAddByCategoria(val idCategoria: Int, order_products: Boolean = false): Screen {
    @Composable
    override fun Content() {
        val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val categoria: CategoriaEntity = categoriaStore.categorias.value.find { it.id == idCategoria }!!

        CommonScreen(title = categoria.nombre) { MainContent(categoria) }.Render()
    }

    @Composable
    fun MainContent(categoria: CategoriaEntity) {
        val productoStore: ProductoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val productos: List<ProductoEntity> = productoStore.productosByCategoria

        val currentCarrito: CarritoEntity = carritoStore.editingCarrito.value!!
        val carritoAndProductos: State<CarritoAndProductsData?> = carritoStore.carritoAndProductos

        LaunchedEffect(categoria.id) {
            productoStore.getProductosByCategoriaId(categoria.id)
            carritoStore.getCarritoAndProductosByCarritoId(currentCarrito.id)
        }

        Column {
            MyScrollableColumn(Modifier.weight(1f)) {
                ProductosAndAmounts(productos, carritoAndProductos, carritoStore, currentCarrito)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { productoStore.setShowAddProductoPopup(true) }) {
                    Text(Literals.ButtonsText.ADD_PRODUCTO + "s")
                }
            }
        }

        if (productoStore.showAddProductoPopup.value) {
            AddProductoPopup(categoria.id)
        }
    }

    @Composable
    fun ProductosAndAmounts(
        productos: List<ProductoEntity>,
        carritoAndProductos: State<CarritoAndProductsData?>,
        carritoStore: CarritoStore,
        currentCarrito: CarritoEntity
    ) {
        productos.forEach { producto ->
            val scope = rememberCoroutineScope()

            // Obtener la cantidad actual del producto en el carrito o 0 si no está
            val cantidad = carritoAndProductos.value?.productosAndCantidades
                ?.find { it.first.id == producto.id }
                ?.second ?: 0

            val rowColorModifier = if (cantidad > 0) Modifier.background(Color.Green, RoundedCornerShape(8.dp)) else Modifier

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(0.6f)) {
                    Text(producto.nombre, Modifier.padding(8.dp))
                }

                Row(
                    modifier = Modifier
                        .weight(0.4f)
                        .then(rowColorModifier),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(cantidad.toString())

                    MyIcons.AddIcon {
                        carritoStore.addProductoToCurrentCarrito(producto)
                        // actualizar el valor de cantidad de forma reactiva
                        scope.launch {
                            carritoStore.getCarritoAndProductosByCarritoId(currentCarrito.id)
                        }
                    }

                    MyIcons.RemoveIcon {
                        carritoStore.substractProductoToCurrentCarrito(producto)
                        // actualizar el valor de cantidad de forma reactiva
                        scope.launch {
                            carritoStore.getCarritoAndProductosByCarritoId(currentCarrito.id)
                        }
                    }
                }
            }

            Spacer(Modifier.height(Tools.height4dp))
        }
    }
}
