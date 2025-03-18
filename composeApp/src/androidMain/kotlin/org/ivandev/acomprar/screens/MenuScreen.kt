package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

object MenuScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.MENU_TITLE
        ) {
            Column {
                Text("Menús --123-", fontSize = 22.sp, color = Color.Black)
            }
        }

        screen.Render()
    }
}