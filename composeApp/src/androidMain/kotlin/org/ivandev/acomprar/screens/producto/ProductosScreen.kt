package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.database.special_classes.CategoriaWithProductos
import org.ivandev.acomprar.models.Producto
import org.ivandev.acomprar.stores.ProductoStore

class ProductosScreen: Screen {
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
        val productoStore: ProductoStore = viewModel()
        val categoriaWithProductosList: State<List<CategoriaWithProductos>?> = productoStore.categoriasWithProductosList

        if (categoriaWithProductosList.value?.isNotEmpty() == true) {
            MyScrollableColumn {
                Column(Modifier.fillMaxSize()) {
                    // Categorías
                    categoriaWithProductosList.value?.forEach { categoriaWithProductos: CategoriaWithProductos ->
                        // Categoria y sus productos
                        Column(Modifier.border(1.dp, Color.Black)) {
                            CategoriaHeader(categoriaWithProductos, productoStore)

                            Column(Modifier.padding(Tools.padding8dp)) {
                                ProductsContainer(categoriaWithProductos.productoEntities, productoStore)
                            }
                        }

                        Spacer(Modifier.height(Tools.height16dp))
                    }
                }
            }
        }
        else {
            Text("No hay categorías disponibles", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }

        // Popups !!!!!!!!!!!
        if (productoStore.addProductoPopup.value) {
            AddProductoPopup(productoStore.productoToAdd.value!!.idCategoria!!)
        }
        if (productoStore.editProductoEntityPopup.value != null) {
            EditProductoPopup(productoStore.editProductoEntityPopup.value!!)
        }
        if (productoStore.deleteProductoEntityPopup.value != null) {
            productoStore.deleteProductoEntity(productoStore.deleteProductoEntityPopup.value!!)
        }
    }

    @Composable
    fun CategoriaHeader(categoriaWithProductos: CategoriaWithProductos, productoStore: ProductoStore) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                categoriaWithProductos.categoriaName,
                style = TextStyle(fontSize = 20.sp, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Black),
                modifier = Modifier.weight(1f)
            )

            MyIcons.AddIcon(
                Modifier
                    .size(24.dp)
                    .clickable {
                        productoStore.setAddProductoPopup(true)

                        productoStore.setProductoToAdd(
                            Producto(null, categoriaWithProductos.categoriaId, null, null, null)
                        )
                    }
            )
        }
    }

    @Composable
    fun ProductsContainer(productoEntities: List<ProductoEntity>?, productoStore: ProductoStore) {
        if (productoEntities.isNullOrEmpty()) {
            Text("Sin productos.")
        }
        else {
            productoEntities.forEach { productoEntity: ProductoEntity ->
                ProductsAndButtonsList(productoEntity, productoStore)
            }
        }
    }

    @Composable
    fun ProductsAndButtonsList(
        productoEntity: ProductoEntity,
        productoStore: ProductoStore
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.weight(0.8f)) {
                Text("${productoEntity.nombre} - ${productoEntity.getCantidadFixed()}")
            }

            Row(Modifier.weight(0.2f)) {
                MyIcons.EditIcon {
                    productoStore.setEditProductoPopup(productoEntity)
                }

                Spacer(Modifier.width(8.dp))

                MyIcons.TrashIcon {
                    productoStore.setProductoToDeletePopup(productoEntity)
                }
            }

            Spacer(Modifier.height(Tools.height8dp))
        }
    }

    private fun deleteProductoById(
        removingId: Int,
        categoriaWithProductos: List<CategoriaWithProductos>,
        setProductosWithCategoria: (List<CategoriaWithProductos>) -> Unit
    ) {
        Database.deleteProductoById(removingId)

        val updatedList = categoriaWithProductos.map { categoria ->
            CategoriaWithProductos(
                categoriaName = categoria.categoriaName,
                categoriaId = categoria.categoriaId,
                productoEntities = categoria.productoEntities?.filter { it.id != removingId }
            )
        }

        setProductosWithCategoria(updatedList)
    }
}
