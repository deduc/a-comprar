package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.ComponentsGetter
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.TopBar

class ConfigurationScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.menuTitle
        ) {
            Column {
                TextSize()
                ImportJSON()
                DeleteAllData()
            }
        }

        screen.Render()
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