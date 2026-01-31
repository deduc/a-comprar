package org.ivandev.acomprar.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TextWhite(text: String) {
    Text(text, style = TextStyle(color = Color.White))
}