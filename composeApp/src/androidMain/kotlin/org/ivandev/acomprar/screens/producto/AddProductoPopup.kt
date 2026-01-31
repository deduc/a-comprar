package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.models.Producto
import org.ivandev.acomprar.stores.CategoriaStore
import org.ivandev.acomprar.stores.ProductoStore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddProductoPopup(idCategoria: Int) {
    val productoStore: ProductoStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    val categoriaStore: CategoriaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

    val categorias: State<List<CategoriaEntity>> = categoriaStore.categorias

    // Preseleccionar la categoría con el ID especificado
    val categoriaEntitySeleccionada = remember { mutableStateOf<CategoriaEntity?>(null) }

    // Expansión del desplegable de las categorias
    val expanded = remember { mutableStateOf(false) }
    val nombre = remember { mutableStateOf("") }
    val cantidad = remember { mutableStateOf("") }
    val marca = remember { mutableStateOf("") }

    LaunchedEffect(idCategoria) {
        val categoriaPorDefecto = categorias.value.find { it.id == idCategoria }
        categoriaEntitySeleccionada.value = categoriaPorDefecto
    }

    if (productoStore.showAddProductoPopup.value) {
        AlertDialog(
            onDismissRequest = {
                productoStore.setAddProductoPopup(false)
                productoStore.setShowAddProductoPopup(false)
           },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoStore.addNewProducto(
                            categoriaEntitySeleccionada.value?.id,
                            nombre.value,
                            cantidad.value,
                            marca.value
                        )
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        productoStore.setShowAddProductoPopup(false)
                    }
                ) { Text("Cancelar") }
            },
            title = { Text("Añadir producto\n") },
            text = {
                Column() {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.TextField(
                            value = nombre.value,
                            onValueChange = { nombre.value = it },
                            label = { androidx.compose.material3.Text("Nombre del producto") }
                        )
                        androidx.compose.material3.TextField(
                            value = cantidad.value,
                            onValueChange = { cantidad.value = it },
                            label = { androidx.compose.material3.Text("Cantidad") },
                        )
                        androidx.compose.material3.TextField(
                            value = marca.value!!,
                            onValueChange = { marca.value = it },
                            label = { androidx.compose.material3.Text("Marca") }
                        )

                        // Desplegable para seleccionar una categoría
                        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
                            androidx.compose.material3.TextField(
                                value = categoriaEntitySeleccionada.value?.nombre ?: "Seleccionar categoría",
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
                                categorias.value.forEach { categoriaEntity: CategoriaEntity ->
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
            }
        )
    }
}