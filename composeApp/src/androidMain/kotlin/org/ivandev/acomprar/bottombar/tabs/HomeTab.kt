package org.ivandev.acomprar.bottombar.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.ivandev.acomprar.bottombar.HomeNavigationButtons
import org.ivandev.acomprar.bottombar.classes.HomeNavigationButton

object HomeTab: Tab {
    private val homeButtons: List<HomeNavigationButton> = HomeNavigationButtons.getHomeButtons()

    override val options: TabOptions
        @Composable
        get() {
            val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Home",
                    icon = homeIcon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
            homeButtons.forEach { button ->
                Button(
                    modifier = button.modifier,
                    onClick = { button.navigatorPush(navigator) }
                ) {
                    Text(text = button.text)
                }
            }
        }
    }
}