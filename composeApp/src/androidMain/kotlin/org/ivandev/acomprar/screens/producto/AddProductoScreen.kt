package org.ivandev.acomprar.screens.producto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import org.ivandev.acomprar.screens.producto.classes.ProductoCheckedMessage

class AddProductoScreen(
    val id: Int
): Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.ADD_PRODUCTO_TITLE
        ) {
            MainContent(id)
        }

        screen.Render()
    }

    @Composable
    fun MainContent(id: Int) {
        AddProductoForm(id)
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AddProductoForm(id: Int) {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val categorias = Database.getAllCategoria()

        // Preseleccionar la categoría con el ID especificado
        val categoriaEntitySeleccionada = remember { mutableStateOf<CategoriaEntity?>(null) }
        // Expansión del desplegable
        val expanded = remember { mutableStateOf(false) }

        LaunchedEffect(id) {
            val categoriaPorDefecto = categorias.find { it.id == id }
            categoriaEntitySeleccionada.value = categoriaPorDefecto
        }

        val nombre = remember { mutableStateOf("") }
        val cantidad = remember { mutableStateOf("") }
        val marca = remember { mutableStateOf("") }

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
                value = marca.value,
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
                    categorias.forEach { categoria ->
                        DropdownMenuItem(onClick = {
                            categoriaEntitySeleccionada.value = categoria
                            expanded.value = false
                        }) {
                            Text(categoria.nombre)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val productoEntity = ProductoEntity(
                        id = null,
                        idCategoria = categoriaEntitySeleccionada.value?.id,
                        nombre = nombre.value,
                        cantidad = cantidad.value,
                        marca = marca.value
                    )
                    addProducto(navigator, productoEntity)
                }
            ) {
                Text(Literals.ADD_TEXT)
            }
        }
    }

    private fun addProducto(navigator: Navigator, productoEntity: ProductoEntity){
        var checking: ProductoCheckedMessage = isProductoOk(productoEntity)

        if (! checking.checkedPassedTest) {
            println(checking.message)
        }

        if (! Database.addProducto(productoEntity)) {
            println("ERROR - No se ha podido añadir la fila por un error desconocido")
            navigator.pop()
            return
        }

        println("Fila añadida en la BDD")
        navigator.pop()
    }

    private fun isProductoOk(productoEntity: ProductoEntity): ProductoCheckedMessage {
        var result: ProductoCheckedMessage = if (
            productoEntity.idCategoria != null &&
            ! productoEntity.nombre.isNullOrEmpty()
        ) {
            ProductoCheckedMessage(true, "")
        } else {
            ProductoCheckedMessage(false, "ERROR: Revisa los datos insertados.")
        }

        return result
    }
}