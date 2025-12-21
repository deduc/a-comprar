package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.components.TextWhite
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.stores.CarritoStore

class CarritosScreen(): Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.CARRITOS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val carritos: SnapshotStateList<CarritoEntity> = carritoStore.carritos

        LaunchedEffect(Dispatchers.IO) {
            carritoStore.getAllCarrito()
        }

        Column(Modifier.fillMaxHeight()) {
            Column(Modifier.weight(1f)) {
                MyScrollableColumn {
                    CarritosList(carritos)
                }
            }

            Row {
                ButtonsPanel(carritoStore)
            }
        }

        Popups(carritoStore)
    }

    @Composable
    fun CarritosList(carritos: SnapshotStateList<CarritoEntity>) {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        if (carritos.isEmpty()) { Text("No hay carritos") }
        else {
            carritos.forEach { it: CarritoEntity ->
                Row(Modifier.fillMaxWidth().then(Tools.styleBorderBlack), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.padding(8.dp).weight(1f)) {
                        Text(it.name, style = Tools.styleTitleBlack)

                        Row(Modifier.padding(4.dp)) {
                            Spacer(Modifier.width(8.dp))
                            Text(it.description)
                        }
                    }

                    Row(Modifier.padding(8.dp)) {
                        MyIcons.ViewIcon {
                            carritoStore.setEditingCarrito(it)
                            navigator.push(EditCarritoScreen(it.id))
                        }
                        Spacer(Tools.spacer8dpWidth)

                        MyIcons.EditIcon {
                            // todo: asignar el nombre y descripcion de ESTE carrito a los datos iniciales del popup
                            carritoStore.setShowAddCarritoPopup(true)
                        }
                        Spacer(Tools.spacer8dpWidth)

                        MyIcons.TrashIcon { carritoStore.deleteCarritoById(it.id) }
                    }
                }

                Spacer(Modifier.height(Tools.height16dp))
            }
        }
    }

    @Composable
    fun ButtonsPanel(carritoStore: CarritoStore) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { carritoStore.setShowAddCarritoPopup(true) }) {
                TextWhite(Literals.ButtonsText.ADD_CARRITO)
            }
        }
    }

    @Composable
    fun Popups(carritoStore: CarritoStore) {
        if (carritoStore.showAddCarritoPopup.value) {
            AddCarritoPopup()
        }
    }
}