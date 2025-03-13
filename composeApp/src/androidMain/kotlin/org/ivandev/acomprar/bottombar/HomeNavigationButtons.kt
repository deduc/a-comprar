package org.ivandev.acomprar.bottombar

import androidx.compose.ui.Modifier
import org.ivandev.acomprar.bottombar.classes.HomeNavigationButton
import org.ivandev.acomprar.bottombar.screens.CarritosScreen
import org.ivandev.acomprar.bottombar.screens.CategoriasScreen
import org.ivandev.acomprar.bottombar.screens.ComidasYCenasScreen
import org.ivandev.acomprar.bottombar.screens.ConfigurationScreen
import org.ivandev.acomprar.bottombar.screens.EstadisticasScreen
import org.ivandev.acomprar.bottombar.screens.MenuScreen
import org.ivandev.acomprar.bottombar.screens.ProductosElaboradosScreen
import org.ivandev.acomprar.bottombar.screens.ProductosScreen

object HomeNavigationButtons {
    val homeTitle: String = "Home"

    val carritosTitle: String = "Carritos de la compra"
    val comidasYCenasTitle: String = "Comidas y cenas"
    val categoriasTitle: String = "Categorías"
    val estadisticasTitle: String = "Estadísticas"
    val menuTitle: String = "Menú"
    val productosElaboradosTitle: String = "Productos elaborados"
    val productosTitle: String = "Productos"
    val configuracionTitle: String = "Configuración"


    fun getHomeButtons(modifier: Modifier = Modifier): List<HomeNavigationButton> {
        return listOf(
            HomeNavigationButton(modifier, carritosTitle, CarritosScreen),
            HomeNavigationButton(modifier, comidasYCenasTitle, ComidasYCenasScreen),
            HomeNavigationButton(modifier, categoriasTitle, CategoriasScreen),
            HomeNavigationButton(modifier, estadisticasTitle, EstadisticasScreen),
            HomeNavigationButton(modifier, menuTitle, MenuScreen),
            HomeNavigationButton(modifier, productosElaboradosTitle, ProductosElaboradosScreen),
            HomeNavigationButton(modifier, productosTitle, ProductosScreen),
            HomeNavigationButton(modifier, configuracionTitle, ConfigurationScreen),
        )
    }
}