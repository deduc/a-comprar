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
import org.ivandev.acomprar.screens.ConfigurationScreen

class TopBar(val backgroundColor: Color? = null) {
    @Composable
    fun Content(title: String = Literals.appName) {
        var backgroundColor: Color
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val settingsIcon: VectorPainter = rememberVectorPainter(Icons.Default.Settings)

        if (this.backgroundColor == null)
            backgroundColor = MaterialTheme.colors.primary
        else
            backgroundColor = this.backgroundColor

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

            if (title == Literals.appName) {
                Button(
                    onClick = {
                        navigator.push(ConfigurationScreen())
                    }
                ) {
                    Icon(
                        painter = settingsIcon,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}