package org.ivandev.acomprar.bottombar.classes

import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import org.ivandev.acomprar.bottombar.interfaces.IHomeNavigationButton

class HomeNavigationButton(
    override val modifier: Modifier = Modifier, // Valor por defecto
    override val text: String,
    override val navigationTab: Tab
) : IHomeNavigationButton
