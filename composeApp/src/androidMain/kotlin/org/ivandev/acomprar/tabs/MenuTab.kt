package org.ivandev.acomprar.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.classes.HomeNavigationButtons

object MenuTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = Literals.menuTitle,
                    icon = homeIcon
                )
            }
        }

    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize().background(Color.Green)) {
            Text("Menús 123132213213213", fontSize = 22.sp, color = Color.Black)
        }
    }
}