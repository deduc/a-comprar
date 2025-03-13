package org.ivandev.acomprar.bottombar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

object CarritosScreen: Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize().background(Color.Green)) {
            Text("Categorías", fontSize = 22.sp, color = Color.Black)
        }
    }
}