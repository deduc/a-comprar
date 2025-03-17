package org.ivandev.acomprar.classes

import androidx.compose.ui.Modifier
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.screens.MenuScreen
import org.ivandev.acomprar.tabs.CarritosTab
import org.ivandev.acomprar.tabs.CategoriasTab
import org.ivandev.acomprar.tabs.ComidasYCenasTab
import org.ivandev.acomprar.tabs.EstadisticasTab
import org.ivandev.acomprar.tabs.MenuTab
import org.ivandev.acomprar.tabs.ProductosElaboradosTab
import org.ivandev.acomprar.tabs.ProductosTab

object HomeNavigationButtons {
    fun getHomeButtons(modifier: Modifier = Modifier): List<HomeNavigationButton> {
        return listOf(
            HomeNavigationButton(modifier, Literals.menuTitle, MenuScreen),
            HomeNavigationButton(modifier, Literals.carritosTitle, CarritosTab),
            HomeNavigationButton(modifier, Literals.comidasYCenasTitle, ComidasYCenasTab),
            HomeNavigationButton(modifier, Literals.categoriasTitle, CategoriasTab),
            HomeNavigationButton(modifier, Literals.productosTitle, ProductosTab),
            HomeNavigationButton(modifier, Literals.productosElaboradosTitle, ProductosElaboradosTab),
            HomeNavigationButton(modifier, Literals.estadisticasTitle, EstadisticasTab),
        )
    }
}