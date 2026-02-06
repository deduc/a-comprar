package org.ivandev.acomprar.screens.mainCarrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.InfoDialog
import org.ivandev.acomprar.components.popups.ConfirmationPopup
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum
import org.ivandev.acomprar.stores.MainCarritoStore

class UserBuyingScreen : Screen {

    @Composable
    override fun Content() {
        CommonScreen(title = Literals.APP_NAME) {
            MainContent()
        }.Render()
    }

    @Composable
    fun MainContent() {
        val mainCarritoStore: MainCarritoStore =
            viewModel(LocalContext.current as ViewModelStoreOwner)

        val boughtProducts = remember { mutableStateListOf<ProductoEntity>() }
        val notBoughtProducts = remember { mutableStateListOf<ProductoEntity>() }

        LaunchedEffect(Dispatchers.IO) {
            mainCarritoStore.loadCarritosToBuyList()
        }

        Column {
            Column(Modifier.weight(0.8f)) {
                ProductsList(
                    mainCarritoStore = mainCarritoStore,
                    boughtProducts = boughtProducts,
                    notBoughtProducts = notBoughtProducts
                )
            }
            Column {
                ButtonsRow(mainCarritoStore)
            }
            Popups(
                mainCarritoStore = mainCarritoStore,
                boughtProducts = boughtProducts,
                notBoughtProducts = notBoughtProducts
            )
        }
    }

    @Composable
    fun Popups(
        mainCarritoStore: MainCarritoStore,
        boughtProducts: List<ProductoEntity>,
        notBoughtProducts: List<ProductoEntity>
    ) {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        when {
            mainCarritoStore.mainCarritoState.value.showStopBuyingPopup -> {
                StopBuyingPopup(
                    mainCarritoStore = mainCarritoStore,
                    boughtProducts = boughtProducts,
                    notBoughtProducts = notBoughtProducts
                )
            }

            mainCarritoStore.mainCarritoState.value.stoppedBuying -> {
                InfoDialog(
                    title = Literals.ToastText.STOPPED_BUYING_OK_TITLE,
                    text = Literals.ToastText.STOPPED_BUYING_OK_TEXT,
                    onDismiss = {
                        mainCarritoStore.setStoppedBuying(false)
                        navigator.popAll()
                    }
                )
            }
        }
    }

    @Composable
    fun StopBuyingPopup(
        mainCarritoStore: MainCarritoStore,
        boughtProducts: List<ProductoEntity>,
        notBoughtProducts: List<ProductoEntity>
    ) {
        ConfirmationPopup(
            text = Literals.TextDialog.STOP_BUYING_CONFIRMATION,
            onAcceptMethod = {
                mainCarritoStore.stopBuying(boughtProducts, notBoughtProducts)
            },
            onDismiss = {
                mainCarritoStore.setShowStopBuyingPopup(false)
            }
        )
    }

    @Composable
    fun ButtonsRow(mainCarritoStore: MainCarritoStore) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { mainCarritoStore.setShowStopBuyingPopup(true) }) {
                Text("Parar de comprar")
            }
        }
    }

    @Composable
    fun ProductsList(
        mainCarritoStore: MainCarritoStore,
        boughtProducts: MutableList<ProductoEntity>,
        notBoughtProducts: MutableList<ProductoEntity>
    ) {
        val categorias = mainCarritoStore.mainCarritoState.value.carritosToBuy

        notBoughtProducts.clear()
        categorias.forEach { categoria ->
            notBoughtProducts.addAll(
                categoria.productoEntities.filter { it !in boughtProducts }
            )
        }

        LazyColumn {
            categorias.forEach { categoria ->
                val productosDisponibles =
                    categoria.productoEntities.filter { it !in boughtProducts }

                if (productosDisponibles.isNotEmpty()) {
                    item {
                        CategoriaHeader(categoria.categoriaName)
                    }

                    items(
                        items = productosDisponibles,
                        key = { it.id }
                    ) { producto ->
                        ProductoRow(
                            producto = producto,
                            onBuy = {
                                boughtProducts.add(producto)
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(Tools.height8dp))
                    }
                }
            }

            if (boughtProducts.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Comprados",
                        fontWeight = FontWeight.Bold,
                        fontSize = Tools.titleFontSize,
                        textDecoration = TextDecoration.Underline
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(
                    items = boughtProducts,
                    key = { it.id }
                ) { producto ->
                    BoughtRow(
                        producto = producto,
                        onUndo = {
                            boughtProducts.remove(producto)
                        }
                    )
                }
            }
        }
    }


    @Composable
    fun CategoriaHeader(nombre: String) {
        Text(
            text = nombre,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight(500),
            fontSize = Tools.titleFontSize
        )
    }

    @Composable
    fun ProductoRow(
        producto: ProductoEntity,
        onBuy: () -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = producto.nombre)

            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(Tools.Colors.GREEN)
                )
            ) {
                Text("Comprar")
            }
        }
    }

    @Composable
    fun BoughtRow(
        producto: ProductoEntity,
        onUndo: () -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = producto.nombre,
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )

            Button(
                onClick = onUndo,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(Tools.Colors.RED)
                )
            ) {
                Text("Deshacer")
            }
        }
    }
}
