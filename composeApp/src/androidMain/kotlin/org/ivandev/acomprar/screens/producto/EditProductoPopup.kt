package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ProductoEntity
import org.ivandev.acomprar.stores.ProductoStore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProductoPopup(productoEntity: ProductoEntity) {
    val productoStore: ProductoStore = viewModel()

    val idCategoria: Int = productoEntity.idCategoria!!
    val categorias = Database.getAllCategoria()

    // Preseleccionar la categoría con el ID especificado
    val categoriaEntitySeleccionada = remember { mutableStateOf(CategoriaEntity(0, "")) }
    // Expansión del desplegable
    val expanded = remember { mutableStateOf(false) }

    LaunchedEffect(idCategoria) {
        val categoriaPorDefecto: CategoriaEntity? = categorias.find { it.id == idCategoria }

        if (categoriaPorDefecto != null) {
            categoriaEntitySeleccionada.value = categoriaPorDefecto
        }
    }

    val nombre = remember { mutableStateOf(productoEntity.nombre) }
    val cantidad = remember { mutableStateOf(productoEntity.getCantidadFixed()) }
    val marca = remember { mutableStateOf(productoEntity.getMarcaFixed()) }

    if (productoStore.editProductoEntityPopup.value != null) {
        AlertDialog(
            onDismissRequest = { productoStore.setEditProductoPopup(null) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newProductoEntity = ProductoEntity(
                            id = productoEntity.id,
                            idCategoria = categoriaEntitySeleccionada.value.id,
                            nombre = nombre.value,
                            cantidad = cantidad.value,
                            marca = marca.value
                        )
                        productoStore.updateProductoById(newProductoEntity)
                        productoStore.setEditProductoPopup(null)
                    }
                ) {
                    Text("Aceptar")
                }
            },
            title = { Text("Editando producto") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = nombre.value,
                        onValueChange = { nombre.value = it },
                        label = { androidx.compose.material3.Text("Nombre del producto") }
                    )
                    TextField(
                        value = cantidad.value,
                        onValueChange = { cantidad.value = it },
                        label = { androidx.compose.material3.Text("Cantidad") },
                    )
                    TextField(
                        value = marca.value!!,
                        onValueChange = { marca.value = it },
                        label = { androidx.compose.material3.Text("Marca") }
                    )

                    // Desplegable para seleccionar una categoría
                    ExposedDropdownMenuBox(
                        expanded = expanded.value,
                        onExpandedChange = { expanded.value = !expanded.value }
                    ) {
                        TextField(
                            value = categoriaEntitySeleccionada.value.nombre,
                            onValueChange = {},
                            label = { androidx.compose.material3.Text("Categoría") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            categorias.forEach { categoriaEntity: CategoriaEntity ->
                                DropdownMenuItem(
                                    onClick = {
                                        categoriaEntitySeleccionada.value = categoriaEntity
                                        expanded.value = false
                                    },
                                    text = { Text(categoriaEntity.nombre) }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
