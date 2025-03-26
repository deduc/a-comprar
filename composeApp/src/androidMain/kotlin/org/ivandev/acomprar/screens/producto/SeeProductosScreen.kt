package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.database.entities.ProductosWithCategoria

class SeeProductosScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.PRODUCTOS_TITLE
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        var productosWithCategoria by remember { mutableStateOf(emptyList<ProductosWithCategoria>()) }

        LaunchedEffect(Unit) {
            productosWithCategoria = withContext(Dispatchers.IO) { Database.getAllProductosByCategoria() }
        }

        MyScrollableColumn {
            Column(Modifier.fillMaxSize().border(color = Color.Black, width = 1.dp)) {
                productosWithCategoria.let { productos ->
                    productos.forEach { it: ProductosWithCategoria ->
                        Header(it, navigator)

                        Column(Modifier.padding(8.dp)) {
                            ProductsList(
                                it.productos,
                                navigator,
                                productosWithCategoria,
                                setProductosWithCategoria = { productosWithCategoria = it }
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun Header(it: ProductosWithCategoria, navigator: Navigator) {
        Row(
            modifier = Modifier.fillMaxWidth().border(color = Color.Black, width = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyTitle(it)

            Button(
                modifier = Modifier,
                onClick = { navigator.push(AddProductoScreen(it.categoriaId)) }
            ) {
                MyIcons.AddIcon(
                    Modifier.size(24.dp)
                )
            }
        }
    }

    @Composable
    private fun MyTitle(it: ProductosWithCategoria) {
        Text(
            it.categoriaName,
            style = TextStyle(
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Black
            )
        )
    }

    @Composable
    private fun ProductsList(
        productosList: List<Producto>?,
        navigator: Navigator,
        productosWithCategoria: List<ProductosWithCategoria>,
        setProductosWithCategoria: (List<ProductosWithCategoria>) -> Unit
    ) {
        if (!productosList.isNullOrEmpty()) {
            productosList.forEach { producto: Producto ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Text(producto.nombre)
                        Spacer(Modifier.width(8.dp))
                        Text("${producto.cantidad}${producto.unidadCantidad}")
                    }

                    Row {
                        MyIcons.EditIcon { editProducto(productosList, producto, navigator) }

                        MyIcons.TrashIcon { deleteProductoById(producto.id!!, productosWithCategoria, setProductosWithCategoria) }
                    }
                }
            }
        } else {
            Row {
                Text("Sin productos")
            }
        }
    }

    private fun editProducto(productosList: List<Producto>, producto: Producto, navigator: Navigator) {
        val productoFiltrado = productosList.find { it.id == producto.id!! }!!
        navigator.push(EditProductoScreen(productoFiltrado))
    }

    private fun deleteProductoById(
        removingId: Int,
        productosWithCategoria: List<ProductosWithCategoria>,
        setProductosWithCategoria: (List<ProductosWithCategoria>) -> Unit
    ) {
        Database.deleteProductoById(removingId)

        val updatedList = productosWithCategoria.map { categoria ->
            ProductosWithCategoria(
                categoriaName = categoria.categoriaName,
                categoriaId = categoria.categoriaId,
                productos = categoria.productos?.filter { it.id != removingId }
            )
        }

        setProductosWithCategoria(updatedList)
    }
}
