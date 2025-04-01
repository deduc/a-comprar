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
import org.ivandev.acomprar.screens.categoria.classes.CategoriaInputResult

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
                    addCategoria(navigator, Categoria(null, text.value))
                }
            ) {
                Text(Literals.ADD_TEXT)
            }
        }
    }

    fun addCategoria(navigator: Navigator, categoria: Categoria){
        val categoriaInput: CategoriaInputResult = isCategoriaOk(categoria)
        var added: Boolean = Database.addCategoria(categoria)

        if (! categoriaInput.isOk) {
            println("ERROR: ${categoriaInput.errorMessage}")
            return
        }

        if (added) {
            println("Fila añadida en la BDD")
            navigator.pop()
        }
        else {
            println("ERROR - No se ha podido añadir la fila")
            navigator.pop()
        }
    }

    fun isCategoriaOk(categoria: Categoria): CategoriaInputResult {
        var result = CategoriaInputResult(true, "")

        if (categoria.nombre.length > 64) {
            result.errorMessage = "64 caracteres como máximo."
            result.isOk = false
        }

        return result
    }
}