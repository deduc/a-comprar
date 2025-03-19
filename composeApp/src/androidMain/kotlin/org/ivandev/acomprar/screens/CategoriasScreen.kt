package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria

class CategoriasScreen : Screen {
    private var categoriasTitle: String
    private var myCategorias: List<Categoria>

    init {
        categoriasTitle = Literals.CATEGORIAS_TITLE
        myCategorias = Database.getAllCategoria()
    }

    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = categoriasTitle
        ) {
            MainContent()
        }

        screen.Render()

    }

    @Composable
    fun MainContent(){
        Column {
            myCategorias.forEach { categoria ->
                Text(categoria.nombre)
            }
        }
    }
}