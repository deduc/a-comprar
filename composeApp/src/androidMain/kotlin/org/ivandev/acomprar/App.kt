package org.ivandev.acomprar

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.screens.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(context: Context) {
    Database.initializeDatabase(context = context)

    MaterialTheme {
        Navigator(
            screen = HomeScreen()
        ) { navigator ->
            SlideTransition(navigator)
        }
    }
}