package org.ivandev.acomprar.bottombar

import androidx.compose.ui.Modifier
import org.ivandev.acomprar.bottombar.classes.HomeNavigationButton
import org.ivandev.acomprar.bottombar.tabs.CategoriasTab
import org.ivandev.acomprar.bottombar.tabs.MenuTab

object HomeNavigationButtons {
    val carritosTitle = "Carritos"
    val comidasCenasTitle = "Comidas/Cenas"
    val categoriasTitle = "Categorías"
    val estadisticasTitle = "Estadísticas"
    val menuTitle = "Menú"
    val productosTitle = "Productos"
    val productosElaboradosTitle = "Productos Elaborados"


    fun getHomeButtons(modifier: Modifier = Modifier): List<HomeNavigationButton> {
        return listOf(
            HomeNavigationButton(modifier, menuTitle, MenuTab),
            HomeNavigationButton(modifier, carritosTitle, CategoriasTab),
            HomeNavigationButton(modifier, comidasCenasTitle, MenuTab),
            HomeNavigationButton(modifier, categoriasTitle, MenuTab),
            HomeNavigationButton(modifier, productosTitle, MenuTab),
            HomeNavigationButton(modifier, estadisticasTitle, MenuTab),
            HomeNavigationButton(modifier, productosElaboradosTitle, MenuTab),
        )
    }
}