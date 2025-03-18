package org.ivandev.acomprar.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.classes.HomeNavigationButtons
import org.ivandev.acomprar.classes.HomeNavigationButton

object HomeTab: Tab {
    private val homeButtons: List<HomeNavigationButton> = HomeNavigationButtons.getHomeButtons(Modifier.fillMaxWidth())

    override val options: TabOptions
    @Composable
    get() {
        val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)

        return remember {
            TabOptions(
                index = 0u,
                title = Literals.HOME_TITLE,
                icon = homeIcon,
            )
        }
    }

    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column (
            Modifier.padding(16.dp)
        ) {
            homeButtons.forEach { button ->
                Button(
                    modifier = button.modifier,
                    onClick = { button.navigatorPush(navigator) }
                ) {
                    Text(text = button.text, style = TextStyle(fontSize = 15.sp))
                }
            }
        }
    }
}