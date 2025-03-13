package org.ivandev.acomprar.bottombar.interfaces

import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab

interface IHomeNavigationButton {
    val modifier: Modifier
    val text: String
    val navigationTab: Tab

    fun navigatorPush(navigator: Navigator) {
        navigator.push(this.navigationTab)
    }
}