package org.ivandev.acomprar.screens.carrito

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

class CarritosScreen(): Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.CARRITOS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        Button(onClick = {}) {
            Text("LALALAA")
        }
        Button(onClick = {}) {
            Text("LALALAA")
        }
    }
}