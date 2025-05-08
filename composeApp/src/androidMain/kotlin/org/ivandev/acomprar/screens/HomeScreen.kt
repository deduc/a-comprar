package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.screens.categoria.CategoriasScreen
import org.ivandev.acomprar.screens.comida.ComidasScreen
import org.ivandev.acomprar.screens.menu.MenuScreen
import org.ivandev.acomprar.screens.producto.ProductosScreen

class HomeScreen: Screen {
    private val homeScreenTitle: String = Literals.HOME_TITLE
    private val appTitle: String = Literals.APP_NAME

    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = homeScreenTitle
        ) {
            MainContent(appTitle)
        }

        screen.Render()
    }

    @Composable
    fun MainContent(title: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            AppTitle(title, false)
            MyNavigationButtons()
        }
    }

    @Composable
    fun AppTitle(title: String, hideSpacers: Boolean) {
        if (hideSpacers) Spacer(Modifier.height(32.dp))

        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                textDecoration = TextDecoration.Underline,
            )
        }

        if (hideSpacers) Spacer(Modifier.height(32.dp))
    }

    @Composable
    fun MyNavigationButtons() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            MyNavButton(title = Literals.MENU_TITLE) { navigator.push(MenuScreen()) }
            MyNavButton(title = Literals.CATEGORIAS_TITLE) { navigator.push(CategoriasScreen()) }
            MyNavButton(title = Literals.PRODUCTOS_TITLE) { navigator.push(ProductosScreen()) }
            MyNavButton(title = Literals.COMIDAS_Y_CENAS_TITLE) { navigator.push(ComidasScreen()) }
        }
    }

    @Composable
    fun MyNavButton(title: String, onClickAction: () -> Unit) {
        Button(onClick = onClickAction, Modifier.fillMaxWidth()) {
            Text(text = title, style = TextStyle(fontSize = 15.sp))
        }
    }
}