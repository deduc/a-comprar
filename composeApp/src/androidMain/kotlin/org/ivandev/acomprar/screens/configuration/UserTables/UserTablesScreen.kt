package org.ivandev.acomprar.screens.configuration.UserTables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen

object UserTablesScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = "Literals.USER_TABLES_TITLE"
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        val userTables: List<String> = listOf("Literals.Database.USER_TABLES")
        Column {
            userTables.forEach { table ->
                Text(table)
            }

        }
    }

}