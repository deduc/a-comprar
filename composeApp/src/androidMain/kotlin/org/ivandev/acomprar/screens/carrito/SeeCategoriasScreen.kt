package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.CategoriaStore

class SeeCategoriasScreen: Screen {
    @Composable
    override fun Content() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoName = carritoStore.carritoAndProductos.value?.carrito?.name ?: Literals.CARRITOS_TITLE

        CommonScreen(title = carritoName) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        val categorias: List<CategoriaEntity> = categoriaStore.categorias.value
        val categoriasGrouped: List<List<CategoriaEntity>> = categorias.chunked(2)
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasGrouped.forEach { categorias ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categorias.forEach { categoria ->
                        Text(
                            text = categoria.nombre,
                            modifier = Modifier.border(1.dp, Color.Black).padding(16.dp).weight(1f).clickable { goAddProductos(categoria.id, navigator) },
                            style = TextStyle(textAlign = TextAlign.Center),
                        )
                    }

                    // Si hay solo una categoría en la fila (cuando el número es impar), añadimos un Spacer
                    if (categorias.size == 1) {
                        Spacer(modifier = Modifier.weight(1f)) // Ocupa el hueco del segundo texto
                    }
                }
            }
        }
    }

    fun goAddProductos(idCategoria: Int, navigator: Navigator) {
        navigator.push(SeeProductosToAddByCategoria(idCategoria))
    }
}