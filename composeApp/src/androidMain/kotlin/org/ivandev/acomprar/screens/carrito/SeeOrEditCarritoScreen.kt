package org.ivandev.acomprar.screens.carrito

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CarritoProductoEntity
import org.ivandev.acomprar.stores.CarritoStore

class SeeOrEditCarritoScreen(val idCarrito: Int): Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.CARRITOS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        val carritoAndProductos: CarritoProductoEntity

        LaunchedEffect(Dispatchers.IO) {
            carritoStore.getCarritoAndProductosByCarritoId(idCarrito)
        }
    }
}