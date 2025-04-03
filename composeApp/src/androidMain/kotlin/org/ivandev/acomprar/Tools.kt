package org.ivandev.acomprar

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Tools {
    var height16dp = 16.dp
    var height4dp = 4.dp
    var padding16dp = 16.dp
    var padding8dp = 8.dp
    var primaryColor = Color(0x6200EE)
    var titleFontSize = 18.sp
    var buttonsSpacer8dp = 8.dp

    var styleTitle = TextStyle(fontSize = this.titleFontSize, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Black)
    var styleTableHeader = TextStyle(fontSize = this.titleFontSize, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
    var styleBorderBlack = Modifier.border(1.dp, Color.Black)
}