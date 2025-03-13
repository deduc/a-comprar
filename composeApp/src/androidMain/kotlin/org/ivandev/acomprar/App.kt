package org.ivandev.acomprar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import acomprar.composeapp.generated.resources.Res
import acomprar.composeapp.generated.resources.compose_multiplatform
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import org.ivandev.acomprar.bottombar.BottomBarScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(
            screen = MainScreen()
        ) { navigator ->
            SlideTransition(navigator)
//            FadeTransition(navigator)
//            ScaleTransition(navigator)
        }
    }
}

class MainScreen: Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button (onClick = {
                navigator.push(SecondScreen())
            }) {
                Text("SecondScreen")
            }
            Spacer(Modifier.width(18.dp))
            Button (onClick = {
                navigator.push(BottomBarScreen())
            }) {
                Text("BottomBar")
            }
        }
    }
}

class SecondScreen: Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Column(
            Modifier.fillMaxWidth().background(color = Color.Cyan),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Esta es la SecondScreen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navigator.pop() }
            ) {
                Text("Volver")
            }
        }
    }
}