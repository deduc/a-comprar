package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Categoria

class AddCategoriaScreen: Screen {
    @Composable
    override fun Content() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val text = remember { mutableStateOf("") }

        Column {
            TextField(
                value = text.value,
                onValueChange = { text.value = it },
            )

            Text(text.value)

            Button(
                onClick = {
                    AddCategoria(navigator, Categoria(null, text.value))
                }
            ) {
                Text(Literals.ADD_TEXT)
            }
        }
    }

    fun AddCategoria(navigator: Navigator, categoria: Categoria){
        var added: Boolean = Database.addCategoria(categoria)

        if (added) {
            println("Fila añadida en la BDD")
            navigator.pop()
        }
        else {
            println("ERROR - No se ha podido añadir la fila")
            navigator.pop()
        }
    }
}