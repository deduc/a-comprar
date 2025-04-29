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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.screens.producto.AddProductoPopup
import org.ivandev.acomprar.screens.producto.EditProductoPopup
import org.ivandev.acomprar.stores.ProductoStore
import java.util.Locale

class SeeCategoriaAndProductsScreen(
    private val categoriaId: Int,
    private val categoriaNombre: String
) : Screen {
    @Composable
    override fun Content() {
        val categoriaName: String = categoriaNombre.replaceFirstChar { it: Char ->
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }

        val productos: MutableState<List<ProductoEntity?>> = remember { mutableStateOf(emptyList()) }

        LaunchedEffect(Unit) {
            productos.value = withContext(Dispatchers.IO) {
                Database.getProductosByCategoriaId(categoriaId )
            }
        }

        val screen = CommonScreen(title = categoriaName) {
            MainContent(categoriaId, categoriaNombre)
        }

        screen.Render()
    }

    @Composable
    private fun MainContent(categoriaId: Int, categoriaNombre: String) {
        val productoStore: ProductoStore = viewModel()

        val selectedProduct = productoStore.productoEntityToEdit
        val productosList: State<List<ProductoEntity>?> = productoStore.productosByCategoria
        var showAddProductoPopup = productoStore.showAddProductoPopup

        LaunchedEffect(categoriaId) {
            productoStore.getProductosByCategoriaId(categoriaId)
        }

        Column {
            Column(Modifier.weight(1f)) {
                val productos = productosList.value

                if (!productos.isNullOrEmpty()) {
                    MyScrollableColumn {
                        productos.forEach { producto ->
                            ProductInfo(producto, selectedProduct)
                        }
                    }
                } else {
                    Text(Literals.NO_DATA_TEXT)
                }
            }

            ButtonsPanel(productoStore)

        }

        // Mostrar el popup si hay una categoría seleccionada
        selectedProduct.value?.let { productoEntity: ProductoEntity ->
            if (productoEntity != null) {
                EditProductoPopup(productoEntity)
            }
        }

        if (showAddProductoPopup.value) {
            AddProductoPopup(categoriaId)
        }
    }

    @Composable
    private fun ButtonsPanel(productoStore: ProductoStore) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
        ) {
            Button(onClick = {
                productoStore.setAddProductoPopup(true)
            }) {
                Text(Literals.ButtonsText.ADD_PRODUCTO)
            }
        }
    }

    @Composable
    private fun ProductInfo(productoEntity: ProductoEntity, selectedProduct: State<ProductoEntity?>) {
        val productoStore: ProductoStore = viewModel()

        Column(Modifier.border(1.dp, Color.Black)) {
            Row(Modifier.border(1.dp, Color.Black).padding(8.dp), Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(productoEntity.nombre, style = TextStyle(fontSize = Tools.titleFontSize))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyIcons.EditIcon { productoStore.setEditProductoPopup(productoEntity) }

                    Spacer(Modifier.width(Tools.buttonsSpacer8dp))

                    MyIcons.TrashIcon { deleteProducto(productoEntity) }
                }
            }
            Row {
                Column(Modifier.padding(Tools.padding8dp)) {
                    val cantidad: String = if(! productoEntity.cantidad.isNullOrEmpty()) productoEntity.cantidad else Literals.SIN_CANTIDAD_TEXT
                    val marca: String = if(! productoEntity.marca.isNullOrEmpty()) productoEntity.marca else Literals.SIN_MARCA_TEXT

                    Text("- $cantidad")
                    Text("- $marca")
                }
            }
        }

        Spacer(Modifier.height(Tools.height16dp))
    }

    private fun deleteProducto(productoEntity: ProductoEntity) {
        var deleted: Boolean = Database.deleteProductoById(productoEntity.id!!)
        // todo: mostrar alerta diciendo producto borrado con exito
    }
}