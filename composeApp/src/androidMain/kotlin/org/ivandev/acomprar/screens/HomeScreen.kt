package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.screens.Pruebas.PruebasScreen
import org.ivandev.acomprar.screens.carrito.CarritosScreen
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
            Spacer(Modifier.height(Tools.height16dp))
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
            MyNavButton(title = Literals.CARRITOS_TITLE, icon = Icons.Default.AddShoppingCart) { navigator.push(CarritosScreen()) }
            MyNavButton(title = Literals.MENU_TITLE, icon = Icons.Default.MenuBook) { navigator.push(MenuScreen()) }
            MyNavButton(title = Literals.COMIDAS_Y_CENAS_TITLE, icon = Icons.Default.Fastfood) { navigator.push(ComidasScreen()) }
            MyNavButton(title = Literals.CATEGORIAS_TITLE, icon = Icons.Default.Category) { navigator.push(CategoriasScreen()) }
            MyNavButton(title = Literals.PRODUCTOS_TITLE, icon = Icons.Default.ShoppingBasket) { navigator.push(ProductosScreen()) }

            MyNavButton(title = "Pantalla de pruebas", icon = Icons.Default.Science) { navigator.push(PruebasScreen()) }
        }
    }

    @Composable
    fun MyNavButton(title: String, icon: ImageVector, onClickAction: () -> Unit) {
        Button(onClick = onClickAction, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = Color.White)
            }
            Box(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(text = title, style = TextStyle(fontSize = 15.sp))
            }
        }
    }
}