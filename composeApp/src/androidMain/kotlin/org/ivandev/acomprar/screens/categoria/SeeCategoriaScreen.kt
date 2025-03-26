package org.ivandev.acomprar.screens.categoria

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria
import org.ivandev.acomprar.database.entities.Producto
import java.util.Locale

class SeeCategoriaScreen(
    private val categoria: Categoria
) : Screen {
    private val productsByCategoria: List<Producto> = Database.getProductosByCategoriaId(categoria.id!!)

    @Composable
    override fun Content() {
        val categoriaName: String = categoria.nombre.replaceFirstChar { it: Char ->
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }

        val screen = CommonScreen(title = categoriaName) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    private fun MainContent() {
        Text("${categoria.id} + ${categoria.nombre}")
        Text(productsByCategoria.toString())
    }
}