package org.ivandev.acomprar.bottombar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.ivandev.acomprar.bottombar.tabs.HomeTab

class BottomBarScreen : Screen {
    @Composable
    override fun Content() {
        val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)
//        val addIcon: VectorPainter = rememberVectorPainter(Icons.Default.Add)
//        val createIcon: VectorPainter = rememberVectorPainter(Icons.Default.Create)
//        val editIcon: VectorPainter = rememberVectorPainter(Icons.Default.Edit)
//        val deleteIcon: VectorPainter = rememberVectorPainter(Icons.Default.Delete)
//        val menuIcon: VectorPainter = rememberVectorPainter(Icons.Default.Menu)
//        val threePointsIcon: VectorPainter = rememberVectorPainter(Icons.Default.MoreVert)

        TabNavigator(
            HomeTab,
            tabDisposable = {
                TabDisposable(
                    it,
                    listOf(HomeTab)
                )
            }
        )
        {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(it.current.options.title) })
                },
                bottomBar = {
                    BottomNavigation {
                        val tabNavigator: TabNavigator = LocalTabNavigator.current

                        BottomNavigationItem(
                            selected = tabNavigator.current.key == HomeTab.key,
                            label = { Text(HomeTab.options.title) },
                            icon = { Icon(painter = homeIcon, contentDescription = null) },
                            onClick = { tabNavigator.current = HomeTab }
                        )
                    }
                },
            )
            { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    Column {
                        CurrentTab()
                    }
                }
            }
        }
    }
}