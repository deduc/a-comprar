package org.ivandev.acomprar.screens.acomprar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.MainCarritoStore

class MainCarrito(): Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.APP_NAME) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val mainCarritoStore: MainCarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritos: SnapshotStateList<CarritoEntity> = carritoStore.carritos

        LaunchedEffect(Dispatchers.IO) {
            carritoStore.getAllCarrito()
        }

        Column() {
            Text("Aqui va el contenido del carrito")
        }
    }
}