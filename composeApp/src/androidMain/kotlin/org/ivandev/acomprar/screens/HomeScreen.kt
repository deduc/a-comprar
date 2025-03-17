package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.ComponentsGetter
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.classes.HomeNavigationButton
import org.ivandev.acomprar.classes.HomeNavigationButtons
import org.ivandev.acomprar.components.CommonScreen

class HomeScreen: Screen {
    private val homeButtons: List<HomeNavigationButton> = HomeNavigationButtons.getHomeButtons(Modifier.fillMaxWidth())
    private val homeScreenTitle: String = Literals.appName

    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = homeScreenTitle
        ) {
            HomeButtonsContainer()
        }

        screen.Render()
    }



    @Composable
    fun HomeButtonsContainer() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column {
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