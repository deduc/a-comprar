package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.DynamicTable
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria

class CategoriasScreen : Screen {
    private val categoriasTitle = Literals.CATEGORIAS_TITLE

    @Composable
    override fun Content() {
        var myCategorias by remember { mutableStateOf(emptyList<Categoria>()) }

        LaunchedEffect(Unit) {
            myCategorias = withContext(Dispatchers.IO) { Database.getAllCategoria() }
        }


        val screen = CommonScreen(title = categoriasTitle) {
            MainContent(
                myCategorias = myCategorias,
                onDeleteCategoria = { id -> myCategorias = deleteCategoriaById(id, myCategorias) }
            )
        }

        screen.Render()
    }

    @Composable
    fun MainContent(myCategorias: List<Categoria>, onDeleteCategoria: (Int) -> Unit) {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            Row(Modifier.weight(1f)) {
                MyScrollableColumn {
                    CategoriasContainer(myCategorias, onDeleteCategoria)
                }
            }

            Row(
                Modifier.fillMaxWidth().weight(0.125f).border(1.dp, Color.Black),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = {
                    navigator.push(AddCategoriaScreen())
                }) {
                    Text("Añadir")
                }
            }
        }
    }

    @Composable
    fun CategoriasContainer(myCategorias: List<Categoria>, onDeleteCategoria: (Int) -> Unit) {
        val columnas = listOf(Literals.Table.NOMBRE_COLUMN, Literals.Table.OPCIONES_COLUMN)
        val navigator: Navigator = LocalNavigator.currentOrThrow

        DynamicTable(
            columnHeaders = columnas,
            rowData = myCategorias,
        ) { categoria: Categoria, column: String ->
            when (column) {
                Literals.Table.NOMBRE_COLUMN -> Text(
                    "${categoria.id} - ${categoria.nombre}",
                    Modifier.clickable { seeCategoriaById(categoria, navigator) }
                )

                Literals.Table.OPCIONES_COLUMN -> Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyIcons.ViewIcon { seeCategoriaById(categoria, navigator) }
                    MyIcons.EditIcon { println(1) }
                    MyIcons.TrashIcon { onDeleteCategoria(categoria.id!!) }
                }
            }
        }
    }

    private fun seeCategoriaById(categoria: Categoria, navigator: Navigator) {
        navigator.push(SeeCategoriaScreen(categoria))
    }

    private fun deleteCategoriaById(removingId: Int, categorias: List<Categoria>): List<Categoria> {
        Database.deleteCategoriaById(removingId)
        return categorias.filter { it.id != removingId }
    }
}
