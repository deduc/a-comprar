package org.ivandev.acomprar.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

class ConfigurationScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.CONFIGURATION_TITLE
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
                text = Literals.SIZE_TEXT,
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
                text = Literals.IMPORT_JSON_TEXT,
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
                text = Literals.DELETE_ALL_DATA_TEXT,
                modifier = Modifier.weight(1f)
            )
            ModifyButton()
        }
    }


    @Composable
    fun ModifyButton(
        onClickFun: () -> Unit = { println(1) },
        text: String = Literals.CHANGE_TEXT
    ) {
        Button(onClick = onClickFun) {
            Text(text)
        }
    }
}