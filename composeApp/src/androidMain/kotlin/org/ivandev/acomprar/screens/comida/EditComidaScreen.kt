package org.ivandev.acomprar.screens.comida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.stores.ComidaStore

class EditComidaScreen(id: Int, nombre: String, tipo: Int): Screen {
    private val comidaToEdit: ComidaEntity = ComidaEntity(id, nombre, tipo)

    @Composable
    override fun Content() {
        val comidaNombre: MutableState<String> = remember { mutableStateOf(comidaToEdit.nombre) }
        val screenTitle: MutableState<String> = remember { mutableStateOf(Literals.ScreenTitles.editComidaTitle(comidaNombre.value)) }

        CommonScreen(screenTitle.value){ MainContent(comidaToEdit, comidaNombre) }.Render()
    }

    @Composable
    fun MainContent(comidaToEdit: ComidaEntity, comidaNombre: MutableState<String>) {
        val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        MyScrollableColumn {
            ComidaNombreFormulary(comidaToEdit, comidaNombre, comidaStore)
        }
    }

    @Composable
    fun ComidaNombreFormulary(comidaToEdit: ComidaEntity, comidaNombre: MutableState<String>, comidaStore: ComidaStore) {
        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = comidaNombre.value,
                onValueChange = { comidaNombre.value = it; comidaToEdit.nombre = it },
                label = { Text("Nombre") }
            )

            MyIcons.SaveIcon {
                val result = comidaStore.updateComidaNombreById(comidaToEdit)

                if (result) Tools.Notifier.showToast("Nuevo nombre guardado.")
                else Tools.Notifier.showToast("ERROR inesperado.")
            }
        }
    }
}