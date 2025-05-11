package org.ivandev.acomprar

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Tools {
    var height4dp = 4.dp
    var height8dp = 8.dp
    var height10dp = 10.dp
    var height16dp = 16.dp

    var padding8dp = 8.dp
    var padding16dp = 16.dp
    var primaryColor = Color(0x6200EE)
    var titleFontSize = 18.sp
    var buttonsSpacer8dp = 8.dp

    var styleTitleUnderlineBlack = TextStyle(fontSize = this.titleFontSize, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Black)
    var styleTitleBlack = TextStyle(fontSize = this.titleFontSize, textDecoration = null, fontWeight = FontWeight.Black)
    var styleTableHeader = TextStyle(fontSize = this.titleFontSize, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
    var styleBorderBlack = Modifier.border(1.dp, Color.Black)

    var spacer8dpWidth = Modifier.width(height8dp)
    var spacer8dpHeight = Modifier.height(height8dp)
    var spacer16dpHeight = Modifier.height(height16dp)

    object Notifier {
        private var appContext: Context? = null

        fun init(context: Context) {
            appContext = context.applicationContext
        }

        fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
            appContext?.let {
                Toast.makeText(it, message, duration).show()
            }
        }
    }
}