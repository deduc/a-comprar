package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.database.entities.ProductoEntity


class EditProductoScreen(
    private val productoEntity: ProductoEntity
): Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.EDIT_PRODUCTO_TITLE
        ) {
            MainContent(productoEntity)
        }

        screen.Render()
    }

    @Composable
    fun MainContent(productoEntity: ProductoEntity) {
        EditProductoForm(productoEntity)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun EditProductoForm(productoEntity: ProductoEntity) {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val idCategoria: Int = productoEntity.idCategoria!!
        val categorias = Database.getAllCategoria()

        // Preseleccionar la categoría con el ID especificado
        val categoriaEntitySeleccionada = remember { mutableStateOf<CategoriaEntity>(CategoriaEntity(0, "")) }
        // Expansión del desplegable
        val expanded = remember { mutableStateOf(false) }

        LaunchedEffect(idCategoria) {
            val categoriaPorDefecto = categorias.find { it.id == idCategoria }
            if (categoriaPorDefecto != null) categoriaEntitySeleccionada.value = categoriaPorDefecto
        }

        val nombre = remember { mutableStateOf(productoEntity.nombre) }
        val cantidad = remember { mutableStateOf(productoEntity.cantidad.toString()) }
        val marca = remember { mutableStateOf(productoEntity.marca) }

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
            )
            TextField(
                value = marca.value!!,
                onValueChange = { marca.value = it },
                label = { Text("Marca") }
            )

            // Desplegable para seleccionar una categoría
            ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
                TextField(
                    value = categoriaEntitySeleccionada.value?.nombre ?: "Seleccionar categoría",
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
                    categorias.forEach { categoriaEntity: CategoriaEntity ->
                        DropdownMenuItem(
                            onClick = {
                                categoriaEntitySeleccionada.value = categoriaEntity
                                expanded.value = false
                            },
                            text = { categoriaEntity.nombre }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val newProductoEntity = ProductoEntity(
                        id = productoEntity.id,
                        idCategoria = categoriaEntitySeleccionada.value.id,
                        nombre = nombre.value,
                        cantidad = cantidad.value,
                        marca = marca.value
                    )
                    updateProducto(newProductoEntity, navigator)
                }
            ) {
                Text(Literals.ADD_TEXT)
            }
        }
    }

    private fun updateProducto(productoEntity: ProductoEntity, navigator: Navigator) {
        val updated = Database.updateProductoById(productoEntity)

        if (updated) {
            println("Binchilin se ha actualizado 1 producto")
        }
        else {
            println("ERROR inesperado, se han actualizado más de 1")
        }

        navigator.pop()
    }
}