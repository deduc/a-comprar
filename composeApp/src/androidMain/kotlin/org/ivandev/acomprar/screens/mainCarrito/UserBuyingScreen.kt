package org.ivandev.acomprar.screens.mainCarrito

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

class UserBuyingScreen: Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.APP_NAME) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {

    }
}