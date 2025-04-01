package org.ivandev.acomprar.screens.menu

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

object MenuScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.PRODUCTOS_TITLE
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {

    }
}