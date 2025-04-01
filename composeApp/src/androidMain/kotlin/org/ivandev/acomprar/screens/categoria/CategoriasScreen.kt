package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.BottomButtonsBar
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.DynamicTable
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.viewModels.CategoriaStore

class CategoriasScreen : Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.CATEGORIAS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val categoriaStore: CategoriaStore = viewModel()

        val categorias = categoriaStore.categorias
        var showPopup by remember { mutableStateOf(false) }

        Column {
            Row(Modifier.weight(1f)) {
                MyScrollableColumn {
                    CategoriasContainer(categorias, categoriaStore)
                }
            }

            Box(Modifier.weight(0.125f)) {
                BottomButtonsBar{
                    Button(onClick = { showPopup = true }) {
                        Text("Añadir")
                    }
                }
            }

//            Row(Modifier.fillMaxWidth().weight(0.125f),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.Bottom
//            ) {
//                Button(onClick = { showPopup = true }) {
//                    Text("Añadir")
//                }
//            }
        }

        if (showPopup) {
            AddCategoriaPopup()
        }
    }

    @Composable
    fun CategoriasContainer(myCategorias: State<List<Categoria>>, categoriaStore: CategoriaStore) {
        val columnas = listOf(Literals.Table.NOMBRE_COLUMN, Literals.Table.OPCIONES_COLUMN)
        val navigator: Navigator = LocalNavigator.currentOrThrow
        var categoriaSeleccionada by remember { mutableStateOf<Categoria?>(null) }

        DynamicTable(columnas, myCategorias.value) { categoria: Categoria, column: String ->
            // when == switch
            when (column) {
                // si variable column == "Nombre", then ->
                Literals.Table.NOMBRE_COLUMN -> Text(
                    "${categoria.id} - ${categoria.nombre}",
                    Modifier.clickable { seeCategoriaById(categoria, navigator) }
                )

                // si variable column == "Opciones", then ->
                Literals.Table.OPCIONES_COLUMN -> Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyIcons.ViewIcon { seeCategoriaById(categoria, navigator) }

                    if(categoria.id != Literals.Database.ID_SIN_CATEGORIA_VALUE) {
                        MyIcons.EditIcon { categoriaSeleccionada = categoria }
                        MyIcons.TrashIcon { deleteCategoria(categoria, categoriaStore) }
                    }
                }
            }
        }

        // Mostrar el popup si hay una categoría seleccionada
        categoriaSeleccionada?.let { categoria ->
            EditCategoriaPopup(categoria)
        }
    }

    private fun seeCategoriaById(categoria: Categoria, navigator: Navigator) {
        navigator.push(SeeCategoriaAndProductsScreen(categoria))
    }

    private fun updateCategoriaById(categoria: Categoria, newCategoriaName: String): Categoria {
        var newCategoria = Categoria(categoria.id, newCategoriaName)
        Database.updateCategoriaById(newCategoria)
        return newCategoria
    }

    private fun deleteCategoria(categoria: Categoria, categoriaStore: CategoriaStore) {
        categoriaStore.deleteCategoria(categoria)
    }
}
