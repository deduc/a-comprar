package org.ivandev.acomprar.bottombar

import androidx.compose.ui.Modifier
import org.ivandev.acomprar.bottombar.classes.HomeNavigationButton
import org.ivandev.acomprar.bottombar.tabs.CarritosTab
import org.ivandev.acomprar.bottombar.tabs.CategoriasTab
import org.ivandev.acomprar.bottombar.tabs.ComidasYCenasTab
import org.ivandev.acomprar.bottombar.tabs.ConfigurationTab
import org.ivandev.acomprar.bottombar.tabs.EstadisticasTab
import org.ivandev.acomprar.bottombar.tabs.MenuTab
import org.ivandev.acomprar.bottombar.tabs.ProductosElaboradosTab
import org.ivandev.acomprar.bottombar.tabs.ProductosTab

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
            HomeNavigationButton(modifier, carritosTitle, CarritosTab),
            HomeNavigationButton(modifier, comidasYCenasTitle, ComidasYCenasTab),
            HomeNavigationButton(modifier, categoriasTitle, CategoriasTab),
            HomeNavigationButton(modifier, estadisticasTitle, EstadisticasTab),
            HomeNavigationButton(modifier, menuTitle, MenuTab),
            HomeNavigationButton(modifier, productosElaboradosTitle, ProductosElaboradosTab),
            HomeNavigationButton(modifier, productosTitle, ProductosTab),
            HomeNavigationButton(modifier, configuracionTitle, ConfigurationTab),
        )
    }
}