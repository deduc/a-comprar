package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.CarritoEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CarritoAndProductsData
import org.ivandev.acomprar.stores.CarritoStore
import org.ivandev.acomprar.stores.CategoriaStore

class EditCarritoScreen(
    // con la inclusion de val, estos 2 objetos son atributos de clase
    // sin val, es solo parametro del constructor
    val currentCarrito: CarritoEntity,
    val carritoStore: CarritoStore
): Screen {
    @Composable
    override fun Content() {
        val carritoName = carritoStore.carritoAndProductos.value?.carrito?.name ?: Literals.CARRITOS_TITLE
        val showDeleteConfirmationDialog = remember { mutableStateOf(false) }

        CommonScreen(
            title = carritoName,
            headerContent = { HeaderButtons { showDeleteConfirmationDialog.value = true } }
        ) { MainContent(carritoStore, currentCarrito) }.Render()

        Popups(carritoStore, showDeleteConfirmationDialog)
    }

    @Composable
    fun HeaderButtons(onDeleteClick: () -> Unit) {
        val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        Row {
            MyIcons.EditIcon(tint = Color.White) {
                // todo: asignar el nombre y descripcion de ESTE carrito a los datos iniciales del popup
                carritoStore._carritoName.value = carritoStore.editingCarrito.value?.name.toString()
                carritoStore._carritoDescription.value = carritoStore.editingCarrito.value?.description.toString()
                carritoStore.setShowEditCarritoPopup(true)
            }
            Spacer(Tools.spacer8dpWidth)

            MyIcons.TrashIcon(tint = Color.White) { onDeleteClick() }
        }
    }

    @Composable
    fun MainContent(carritoStore: CarritoStore, currentCarrito: CarritoEntity) {
        val carritoAndProductos: State<CarritoAndProductsData?> = carritoStore.carritoAndProductos

        LaunchedEffect(currentCarrito.id) {
            carritoStore.getCarritoAndProductosByCarritoId(currentCarrito.id)
        }

        Column {
            Column(Modifier.weight(1f)) {
                if (carritoAndProductos.value != null) {
                    CarritoData(carritoAndProductos.value!!, carritoStore)
                }
            }

            Row {
                ButtonsPanel()
            }
        }
    }

    @Composable
    fun ButtonsPanel() {
        val navigator = LocalNavigator.currentOrThrow

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { navigator.push(SeeCategoriasScreen()) }) {
                Text(Literals.ButtonsText.ADD_PRODUCTO)
            }
        }
    }

    @Composable
    fun CarritoData(carritoAndProductos: CarritoAndProductsData, carritoStore: CarritoStore) {
        val carrito: CarritoEntity = carritoAndProductos.carrito

        CarritoDescription(carrito.description)

        Spacer(Tools.spacer16dpHeight)

        CarritoProductsData(carritoAndProductos, carritoStore)
    }

    @Composable
    fun CarritoDescription(description: String) {
        Row(Modifier.border(1.dp, Color.Black).fillMaxWidth()) {
            Text(description, Modifier.border(1.dp, Color.Black).fillMaxWidth().padding(8.dp))
        }
    }

    @Composable
    fun CarritoProductsData(
        carritoAndProductos: CarritoAndProductsData,
        carritoStore: CarritoStore
    ) {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        val productosAndCantidades: List<Pair<ProductoEntity, Int>> = carritoAndProductos.productosAndCantidades

        // Agrupar productos por categoría
        val productosPorCategoria: Map<Int, List<Pair<ProductoEntity, Int>>> = productosAndCantidades.groupBy { it.first.idCategoria }

        MyScrollableColumn {
            Column {
                if (productosAndCantidades.isEmpty()) {
                    Text("Sin productos.")
                }
                else {
                    productosPorCategoria.forEach { (categoriaId: Int, productosAndCantidades: List<Pair<ProductoEntity, Int>>) ->
                        val categoriaNombre = categoriaStore.getCategoriaNameById(categoriaId, categoriaStore.categorias.value)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(categoriaNombre, style = Tools.styleTitleUnderlineBlack)

                            Spacer(Tools.spacer8dpWidth)

                            Button(
                                onClick = { navigator.push(SeeProductosToAddByCategoria(categoriaId, order_products=true)) },
                                modifier = Modifier.height(32.dp).defaultMinSize(minWidth = 80.dp).padding(horizontal = 4.dp, vertical = 0.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Ver", fontSize = 12.sp)
                            }
                        }

                        Spacer(Tools.spacer8dpHeight)

                        productosAndCantidades.forEach { (producto: ProductoEntity, cantidad: Int) ->
                            val cantidadFixed = carritoStore.doFixCantidadStr(producto.cantidad, cantidad)

                            Row(Modifier.fillMaxWidth(0.7f), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = producto.nombre, modifier = Modifier.weight(1f), maxLines = Int.MAX_VALUE, overflow = TextOverflow.Clip)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(cantidadFixed, Modifier.align(Alignment.CenterVertically))
                            }
                            Spacer(Modifier.height(Tools.height8dp))
                        }
                        Spacer(Modifier.height(Tools.height8dp))
                    }
                }
            }
            //fin column
        }
        // fin MyScrollableColumn
    }

    @Composable
    fun Popups(carritoStore: CarritoStore, showDeleteConfirmationDialog: MutableState<Boolean>) {
        val navigator = LocalNavigator.currentOrThrow

        if (carritoStore.showAddCarritoPopup.value) {
            AddCarritoPopup()
        }

        if (carritoStore.showEditCarritoPopup.value) {
            EditCarritoPopup()
        }


        if (showDeleteConfirmationDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmationDialog.value = false },
                title = { Text("Confirmar borrado") },
                text = { Text("¿Estás seguro de que quieres borrar el carrito?") },
                confirmButton = {
                    Button(onClick = {
                        carritoStore.deleteCarritoById(currentCarrito.id)
                        showDeleteConfirmationDialog.value = false
                        navigator.pop()
                    }) {
                        Text("Borrar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirmationDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}