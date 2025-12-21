package org.ivandev.acomprar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.ivandev.acomprar.Tools

// Clase composable que incluye TopBar y BottomBar, y acepta contenido dinámico en el medio
class CommonScreen(
    private val title: String,
    private val headerContent: @Composable (() -> Unit)? = null,
    private val dynamicContent: @Composable () -> Unit
) {
    @Composable
    fun Render() {
        val topBar = TopBar()
        val bottomBar = BottomBar()

        Column(modifier = Modifier.fillMaxSize()) {
            topBar.Content(title = title, headerContent = headerContent)

            // Contenido dinámico (puede ser cualquier cosa que se pase a este parámetro)
            Column(
                Modifier
                    .padding(Tools.padding16dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Aquí se coloca el contenido que se pasa como parámetro
                dynamicContent()
            }

            bottomBar.Content()
        }
    }
}
