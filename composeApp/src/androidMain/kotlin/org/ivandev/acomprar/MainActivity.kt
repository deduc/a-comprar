package org.ivandev.acomprar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(context = this)
        }
    }

    @Composable
    fun App(context: Context) {
        Database.initializeDatabase(context)
        Tools.Notifier.init(context)

        val screenToShow = remember { HomeScreen() }
        MaterialTheme {
            Navigator(screen = screenToShow) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
