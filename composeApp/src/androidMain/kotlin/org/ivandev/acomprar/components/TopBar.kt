package org.ivandev.acomprar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.screens.configuration.ConfigurationScreen

class TopBar(
    private val backgroundColor: Color? = null
) {
    @Composable
    fun Content(title: String, headerContent: @Composable (() -> Unit)? = null) {
        val backgroundColor: Color = this.backgroundColor ?: MaterialTheme.colors.primary
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val settingsIcon: VectorPainter = rememberVectorPainter(Icons.Default.Settings)
        val arrowBackIcon: VectorPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column 1: Back button or empty space
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (title != Literals.HOME_TITLE) {
                    GoBackButton(navigator, arrowBackIcon)
                }
            }

            // Column 2: Title
            Text(
                text = title,
                color = Color.White,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.6f)
            )

            // Column 3: Action buttons
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (title == Literals.HOME_TITLE) {
                    ConfigButton(navigator, settingsIcon)
                } else {
                    headerContent?.invoke()
                }
            }
        }
    }

    @Composable
    fun ConfigButton(navigator: Navigator, settingsIcon: VectorPainter) {
        Button(onClick = { navigator.push(ConfigurationScreen()) }) {
            Icon(
                painter = settingsIcon,
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }

    @Composable
    fun GoBackButton(navigator: Navigator, arrowBackIcon: VectorPainter) {
        Button(onClick = { navigator.pop() }) {
            Icon(
                painter = arrowBackIcon,
                contentDescription = "Go back",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}