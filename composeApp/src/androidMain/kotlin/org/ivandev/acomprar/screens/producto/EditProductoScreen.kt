package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto


class EditProductoScreen(
    private val producto: Producto
): Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.EDIT_PRODUCTO_TITLE
        ) {
            MainContent(producto)
        }

        screen.Render()
    }

    @Composable
    fun MainContent(producto: Producto) {
        EditProductoForm(producto)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun EditProductoForm(producto: Producto) {
        val id: Int = producto.idCategoria!!
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val categorias = Database.getAllCategoria()

        // Preseleccionar la categoría con el ID especificado
        val categoriaSeleccionada = remember { mutableStateOf<Categoria?>(null) }
        // Expansión del desplegable
        val expanded = remember { mutableStateOf(false) }

        LaunchedEffect(id) {
            val categoriaPorDefecto = categorias.find { it.id == id }
            categoriaSeleccionada.value = categoriaPorDefecto
        }

        val nombre = remember { mutableStateOf(producto.nombre) }
        val cantidad = remember { mutableStateOf(producto.cantidad.toString()) }
        val unidadCantidad = remember { mutableStateOf(producto.unidadCantidad) }
        val marca = remember { mutableStateOf(producto.marca) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
                label = { Text("Nombre del producto") }
            )
            TextField(
                value = cantidad.value,
                onValueChange = { cantidad.value = it },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = unidadCantidad.value,
                onValueChange = { unidadCantidad.value = it },
                label = { Text("Unidad de cantidad") }
            )
            TextField(
                value = marca.value,
                onValueChange = { marca.value = it },
                label = { Text("Marca") }
            )

            // Desplegable para seleccionar una categoría
            ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
                TextField(
                    value = categoriaSeleccionada.value?.nombre ?: "Seleccionar categoría",
                    onValueChange = {},
                    label = { Text("Categoría") },
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
//                        DropdownMenuItem(onClick = {
//                            categoriaSeleccionada.value = categoria
//                            expanded.value = false
//                        }) {
//                            Text(categoria.nombre)
//                        }
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

            Button(
                onClick = {
                    val producto = Producto(
                        id = null,
                        idCategoria = categoriaSeleccionada.value?.id,
                        nombre = nombre.value,
                        cantidad = cantidad.value.toFloatOrNull() ?: 0f,
                        unidadCantidad = unidadCantidad.value,
                        marca = marca.value
                    )
//                    addProducto(navigator, producto)
                }
            ) {
                Text(Literals.ADD_TEXT)
            }
        }
    }

}