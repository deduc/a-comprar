package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
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
        var productosByCategoria by remember { mutableStateOf(emptyList<ProductosWithCategoria>()) }

        LaunchedEffect(Unit) {
            productosByCategoria = withContext(Dispatchers.IO) { Database.getAllProductosByCategoria() }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            productosByCategoria.let { lista ->
                lista.forEach { it: ProductosWithCategoria ->
                    Text(it.categoriaName)

                    if (! it.productos.isNullOrEmpty()) {
                        it.productos.forEach { producto: Producto ->
                            Text(producto.nombre)
                        }
                    }
                    else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sin productos", Modifier.weight(1f))
                            Button(onClick = { navigator.push(AddProductoScreen(it.categoriaId)) }) {
                                Text("Añadir")
                            }
                        }
                    }
                }
            }
        }
    }
}