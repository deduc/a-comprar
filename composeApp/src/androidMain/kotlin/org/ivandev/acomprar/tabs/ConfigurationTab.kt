package org.ivandev.acomprar.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.ivandev.acomprar.classes.HomeNavigationButtons
import org.ivandev.acomprar.Literals

class ConfigurationTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val homeIcon: VectorPainter = rememberVectorPainter(Icons.Default.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = Literals.carritosTitle,
                    icon = homeIcon
                )
            }
        }

    @Composable
    override fun Content() {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            TextSize()
            ImportJSON()
            DeleteAllData()
        }
    }

    @Composable
    fun TextSize() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Literals.sizeText,
                modifier = Modifier.weight(1f)
            )
            ModifyButton()
        }
    }

    @Composable
    fun ImportJSON() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Literals.importJsonText,
                modifier = Modifier.weight(1f)
            )
            ModifyButton()
        }
    }

    @Composable
    fun DeleteAllData() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Literals.deleteAllDataText,
                modifier = Modifier.weight(1f)
            )
            ModifyButton()
        }
    }


    @Composable
    fun ModifyButton(
        onClickFun: () -> Unit = { println(1) },
        text: String = Literals.changeText
    ) {
        Button(onClick = onClickFun) {
            Text(text)
        }
    }
}