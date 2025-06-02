package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.CategoriaStore
import org.ivandev.acomprar.stores.ProductoStore

class SeeProductosToAddByCategoria(val idCategoria: Int): Screen {
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

        LaunchedEffect(categoria.id) {
            productoStore.getProductosByCategoriaId(categoria.id)
        }

        MyScrollableColumn(Modifier.padding(8.dp)) {
            productos.forEach { producto: ProductoEntity ->
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(producto.nombre)

                    MyIcons.AddIcon { carritoStore.addProductoToCurrentCarrito(producto) }
                }
            }
        }
    }
}