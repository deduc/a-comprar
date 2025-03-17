package org.ivandev.acomprar.classes.interfaces

import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

interface IHomeNavigationButton {
    val modifier: Modifier
    val text: String
    val navigationScreen: Screen

    fun navigatorPush(navigator: Navigator) {
        navigator.push(this.navigationScreen)
    }
}