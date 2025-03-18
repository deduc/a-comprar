package org.ivandev.acomprar.components

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen

class InputTextPrueba {
    @Composable
    fun Content() {
        val text = remember { mutableStateOf("") }

        TextField(
            value = text.value,
            onValueChange = { text.value = it },
        )
        Text(text.value)
    }
}
