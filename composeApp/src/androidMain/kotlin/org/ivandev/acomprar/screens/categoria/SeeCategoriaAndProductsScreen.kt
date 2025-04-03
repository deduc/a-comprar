package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto
import org.ivandev.acomprar.screens.producto.AddProductoScreen
import org.ivandev.acomprar.screens.producto.EditProductoPopup
import java.util.Locale

class SeeCategoriaAndProductsScreen(
    private val categoria: Categoria
) : Screen {
    @Composable
    override fun Content() {
        val categoriaName: String = categoria.nombre.replaceFirstChar { it: Char ->
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }

        val productos: MutableState<List<Producto?>> = remember { mutableStateOf(emptyList()) }

        LaunchedEffect(Unit) {
            productos.value = withContext(Dispatchers.IO) {
                Database.getProductosByCategoriaId(categoria.id!! )
            }
        }

        val screen = CommonScreen(title = categoriaName) {
            MainContent(categoria, productos)
        }

        screen.Render()
    }

    @Composable
    private fun MainContent(categoria: Categoria, productos: MutableState<List<Producto?>>) {
        var selectedProduct = remember { mutableStateOf<Producto?>(null) }
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            MyScrollableColumn {
                Column(Modifier.weight(1f)) {
                    if (productos.value.size > 0) {
                        Column {
                            productos.value.forEach { producto: Producto? ->
                                ProductInfo(producto!!, selectedProduct)
                            }
                        }
                    }
                    else {
                        Text(Literals.NO_DATA_TEXT)
                    }
                }

                ButtonsPanel(categoria, navigator)
            }

            // Mostrar el popup si hay una categoría seleccionada
            selectedProduct.value?.let { producto: Producto ->
                if (producto != null) EditProductoPopup(producto)
            }
        }
    }

    @Composable
    private fun ButtonsPanel(categoria: Categoria, navigator: Navigator) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
        ) {
            Button(onClick = {
                navigator.push(AddProductoScreen(categoria.id!!))
            }) {
                androidx.compose.material.Text("Añadir")
            }
        }
    }

    @Composable
    private fun ProductInfo(producto: Producto, selectedProduct: MutableState<Producto?>) {
        Column(Modifier.border(1.dp, Color.Black)) {
            Row(Modifier.border(1.dp, Color.Black).padding(8.dp), Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(producto.nombre, style = TextStyle(fontSize = Tools.titleFontSize))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyIcons.EditIcon { selectedProduct.value = producto }
                    Spacer(Modifier.width(Tools.buttonsSpacer8dp))
                    MyIcons.TrashIcon { deleteProducto(producto) }
                }
            }
            Row {
                Column(Modifier.padding(Tools.padding8dp)) {
                    val cantidad: String = if(! producto.cantidad.isNullOrEmpty()) producto.cantidad else Literals.SIN_CANTIDAD_TEXT
                    val marca: String = if(! producto.marca.isNullOrEmpty()) producto.marca else Literals.SIN_MARCA_TEXT

                    Text("- $cantidad")
                    Text("- ${marca}")
                }
            }
        }

        Spacer(Modifier.height(Tools.height16dp))
    }

    private fun deleteProducto(producto: Producto) {
        var deleted: Boolean = Database.deleteProductoById(producto.id!!)
        // todo: mostrar alerta diciendo producto borrado con exito
    }
}