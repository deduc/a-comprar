package org.ivandev.acomprar.screens.mainCarrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import org.ivandev.acomprar.components.BigButtonIconText
import org.ivandev.acomprar.components.CarritoContainer
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.ConfirmationPopup
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum
import org.ivandev.acomprar.screens.carrito.EditCarritoScreen
import org.ivandev.acomprar.screens.carrito.PopupEditCarrito
import org.ivandev.acomprar.screens.carrito.SeeCategoriasScreen
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.MainCarritoStore

class MainCarritoScreen(): Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.APP_NAME) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val mainCarritoStore: MainCarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val userIsBuying: UserActionsEntity = mainCarritoStore.mainCarritoState.value.userBuying

        LaunchedEffect(Dispatchers.IO) {
            mainCarritoStore.getAllCarrito()
            mainCarritoStore.checkUserBuying()
        }

        Column {
            Column(Modifier.weight(0.7f)) {
                CarritosContainer(mainCarritoStore, carritoStore)
            }

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (userIsBuying.actionValue == UserBuyingEnum.USER_IS_NOT_BUYING) {
                    MyAcomprarButton(mainCarritoStore)
                }
                else {
                    StopBuyingButton(mainCarritoStore)
                }

                Spacer(Tools.spacer8dpHeight)
                ButtonsRow(mainCarritoStore, carritoStore)
            }
        }

        Popups(carritoStore, mainCarritoStore)
    }

    @Composable
    fun StopBuyingButton(mainCarritoStore: MainCarritoStore) {
        Button(onClick = { mainCarritoStore.setShowStopBuyingPopup(true) }) {
            Text("Parar de comprar!!")
        }
    }

    @Composable
    fun ButtonsRow(mainCarritoStore: MainCarritoStore, carritoStore: CarritoStore) {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Button(onClick = {
                    carritoStore.setEditingCarritoUsingId(Literals.Database.HardcodedValues.CARRITO_BASTARDO_ID)
                    navigator.push(SeeCategoriasScreen())
                }) {
                    Text(Literals.ButtonsText.ADD_PRODUCTO)
                }
            }

//            item {
//                Button(onClick = {
//                    Tools.Notifier.showToast("Comienzas a comprar = ${mainCarritoStore.mainCarritoState.value.userBuying.actionValue}")
//                }) {
//                    Text("Mas botones")
//                }
//            }
        }
    }

    @Composable
    fun MyAcomprarButton(mainCarritoStore: MainCarritoStore) {
        Column(Modifier.fillMaxWidth(0.8f)) {
            BigButtonIconText(
                onClick = { mainCarritoStore.setShowAComprarPopup(true) },
                buttonPaddingDp = 0,
                containerVerticalPaddingDp = 8,
                textSizeDp = 16
            )
        }
    }

    @Composable
    fun CarritosContainer(mainCarritoStore: MainCarritoStore, carritoStore: CarritoStore) {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val carritos: List<CarritoEntity> = mainCarritoStore.mainCarritoState.value.carritos

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = carritos,
                key = { it.id }
            ) { carrito: CarritoEntity ->
                CarritoContainer(carrito) {
                    RightIcons(carrito, carritoStore, navigator)
                }
            }
        }
    }

    @Composable
    fun RightIcons(
        carrito: CarritoEntity,
        carritoStore: CarritoStore,
        navigator: Navigator,
    ) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            MyIcons.ViewIcon {
                carritoStore.setEditingCarrito(carrito)
                carrito.printData()
                navigator.push(EditCarritoScreen(carrito, carritoStore))
            }

            Spacer(Modifier.width(Tools.height16dp))

            if (carrito.id != Literals.Database.HardcodedValues.MAIN_CARRITO_ID) {
                MyIcons.RemoveShoppingCartIcon {
                    carritoStore.setShowDeleteCarritoPopup(true)
                    carritoStore.setDeletingCarrito(carrito)
                }
            }
        }
    }

    @Composable
    fun Popups(carritoStore: CarritoStore, mainCarritoStore: MainCarritoStore) {
        if (carritoStore.showEditCarritoPopup.value) {
            PopupEditCarrito()
        }
        else if (carritoStore.showDeleteCarritoPopup.value) {
            ConfirmationPopup(
                text = Literals.ToastText.DELETING_CARRITO_FROM_MAIN_CARRITO,
                onAcceptMethod = {
                    mainCarritoStore.deleteCarritoFromMainCarrito(carritoStore.deletingCarrito.value!!.id, carritoStore)
                    carritoStore.setShowDeleteCarritoPopup(false)
                    carritoStore.setDeletingCarrito(null)
                },
                onDismiss = {
                    carritoStore.setShowDeleteCarritoPopup(false)
                    carritoStore.setDeletingCarrito(null)
                }
            )
        }
        else if (mainCarritoStore.mainCarritoState.value.showAComprarPopup) {
            ConfirmationPopup(
                text = Literals.TextDialog.A_COMPRAR_CONFIRMATION,
                onAcceptMethod = {
                    mainCarritoStore.setShowAComprarPopup(false)
                    mainCarritoStore.setUserIsBuying(UserBuyingEnum.USER_IS_BUYING)
                },
                onDismiss = {
                    mainCarritoStore.setShowAComprarPopup(false)
                }
            )
        }
        else if (mainCarritoStore.mainCarritoState.value.showStopBuyingPopup) {
            ConfirmationPopup(
                text = Literals.TextDialog.STOP_BUYING_CONFIRMATION,
                onAcceptMethod = {
                    mainCarritoStore.setShowStopBuyingPopup(false)
                    mainCarritoStore.setUserIsBuying(UserBuyingEnum.USER_IS_NOT_BUYING)
                },
                onDismiss = {
                    mainCarritoStore.setShowStopBuyingPopup(false)
                }
            )
        }
    }
}
