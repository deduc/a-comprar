package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProductoPopup(producto: Producto, myProducto: MutableState<List<Producto?>>) {
    var showPopup by remember { mutableStateOf(true) }

    val idCategoria: Int = producto.idCategoria!!
    val categorias = Database.getAllCategoria()

    // Preseleccionar la categoría con el ID especificado
    val categoriaSeleccionada = remember { mutableStateOf<Categoria?>(null) }
    // Expansión del desplegable
    val expanded = remember { mutableStateOf(false) }

    LaunchedEffect(idCategoria) {
        val categoriaPorDefecto = categorias.find { it.id == idCategoria }
        categoriaSeleccionada.value = categoriaPorDefecto
    }

    val nombre = remember { mutableStateOf(producto.nombre) }
    val cantidad = remember { mutableStateOf(producto.cantidad.toString()) }
    val unidadCantidad = remember { mutableStateOf(producto.unidadCantidad) }
    val marca = remember { mutableStateOf(producto.marca) }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newProducto = Producto(
                            id = producto.id,
                            idCategoria = categoriaSeleccionada.value?.id,
                            nombre = nombre.value,
                            cantidad = cantidad.value.toFloatOrNull() ?: 0f,
                            unidadCantidad = unidadCantidad.value,
                            marca = marca.value
                        )
                        updateProducto(newProducto)

                        showPopup = false
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
                    androidx.compose.material3.TextField(
                        value = nombre.value,
                        onValueChange = { nombre.value = it },
                        label = { androidx.compose.material3.Text("Nombre del producto") }
                    )
                    androidx.compose.material3.TextField(
                        value = cantidad.value,
                        onValueChange = { cantidad.value = it },
                        label = { androidx.compose.material3.Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    androidx.compose.material3.TextField(
                        value = unidadCantidad.value,
                        onValueChange = { unidadCantidad.value = it },
                        label = { androidx.compose.material3.Text("Unidad de cantidad") }
                    )
                    androidx.compose.material3.TextField(
                        value = marca.value,
                        onValueChange = { marca.value = it },
                        label = { androidx.compose.material3.Text("Marca") }
                    )

                    // Desplegable para seleccionar una categoría
                    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
                        androidx.compose.material3.TextField(
                            value = categoriaSeleccionada.value?.nombre ?: "Seleccionar categoría",
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
                            categorias.forEach { categoria: Categoria ->
                                DropdownMenuItem(
                                    onClick = {
                                        categoriaSeleccionada.value = categoria
                                        expanded.value = false
                                    },
                                    text = { categoria.nombre }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

fun updateProducto(producto: Producto) {
    val updated = Database.updateProductoById(producto)

    if (updated) {
        println("Binchilin se ha actualizado 1 producto")
    }
    else {
        println("ERROR inesperado, se han actualizado más de 1")
    }
}
