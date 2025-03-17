package org.ivandev.acomprar.classes

import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.classes.interfaces.IHomeNavigationButton

class HomeNavigationButton(
    override val modifier: Modifier = Modifier, // Valor por defecto
    override val text: String,
    override val navigationScreen: Screen
) : IHomeNavigationButton
