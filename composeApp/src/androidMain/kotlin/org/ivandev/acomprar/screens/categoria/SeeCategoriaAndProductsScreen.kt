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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
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

        val screen = CommonScreen(title = categoriaName) {
            MainContent(categoriaId)
        }

        screen.Render()
    }

    @Composable
    private fun MainContent(categoriaId: Int) {
        val productoStore: ProductoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
        productoStore.getProductosByCategoriaId(categoriaId)

        val productosByCategoria: SnapshotStateList<ProductoEntity> = productoStore.productosByCategoria

        Column {
            Column(Modifier.weight(1f)) {
                if (productosByCategoria.isNotEmpty()) {
                    MyScrollableColumn {
                        productosByCategoria.forEach { producto: ProductoEntity ->
                            ProductInfo(producto)
                        }
                    }
                } else {
                    Text(Literals.NO_DATA_TEXT)
                }
            }

            ButtonsPanel()
        }

        if (productoStore.showEditProductoPopup.value && productoStore.editProductoEntityPopup.value != null) {
            EditProductoPopup(productoStore.editProductoEntityPopup.value!!)
        }

        if (productoStore.showAddProductoPopup.value) {
            AddProductoPopup(categoriaId)
        }
    }

    @Composable
    private fun ButtonsPanel() {
        val productoStore: ProductoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
        ) {
            Button(onClick = {
                productoStore.setAddProductoPopup(true)
                productoStore.setShowAddProductoPopup(true)
            }) {
                Text(Literals.ButtonsText.ADD_PRODUCTO)
            }
        }
    }

    @Composable
    private fun ProductInfo(productoEntity: ProductoEntity) {
        val productoStore: ProductoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        Column(Tools.styleBorderBlack) {
            Row(Modifier.border(1.dp, Color.Black).padding(8.dp), Arrangement.SpaceBetween) {
                Row(Modifier.weight(1f)) {
                    Text(productoEntity.nombre, style = TextStyle(fontSize = Tools.titleFontSize))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyIcons.EditIcon {
                        productoStore.setEditProductoPopup(productoEntity)
                        productoStore.setShowEditProductoPopup(true)
                    }
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