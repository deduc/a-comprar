package org.ivandev.acomprar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyScrollableColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxSize().fillMaxWidth().verticalScroll(rememberScrollState()).then(modifier)) {
        content()
    }
}
