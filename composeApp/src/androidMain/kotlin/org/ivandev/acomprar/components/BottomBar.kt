package org.ivandev.acomprar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Tools

class BottomBar(val backgroundColor: Color? = null) {
    @Composable
    fun Content() {
        var backgroundColor: Color
        val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)
        val navigator: Navigator = LocalNavigator.currentOrThrow

        if (this.backgroundColor == null)
            backgroundColor = Tools.primaryColor
        else
            backgroundColor = this.backgroundColor

        Row(
            modifier = Modifier.fillMaxWidth().background(backgroundColor),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navigator.popUntilRoot()},
                modifier = Modifier.background(Color.Red).fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = homeIcon,
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )

                    Text(
                        "Home",
                        style = TextStyle(fontSize = 12.sp),
                    )
                }
            }
        }
    }
}