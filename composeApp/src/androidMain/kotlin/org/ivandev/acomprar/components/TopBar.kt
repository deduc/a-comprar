package org.ivandev.acomprar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
    fun Content(title: String) {
        val backgroundColor: Color = this.backgroundColor ?: MaterialTheme.colors.primary
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val settingsIcon: VectorPainter = rememberVectorPainter(Icons.Default.Settings)
        val arrowBackIcon: VectorPainter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack)

        if (title == Literals.HOME_TITLE) {
            Row(
                modifier = Modifier.fillMaxWidth().background(backgroundColor).height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                ConfigButton(navigator, settingsIcon)
            }

        }
        else {
            Row(
                modifier = Modifier.fillMaxWidth().background(backgroundColor).height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GoBackButton(navigator, arrowBackIcon)

                Text(
                    text = title,
                    color = Color.White,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
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