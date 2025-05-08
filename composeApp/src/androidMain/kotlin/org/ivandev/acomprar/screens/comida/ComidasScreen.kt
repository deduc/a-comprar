package org.ivandev.acomprar.screens.comida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.stores.ComidaStore

class ComidasScreen: Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.COMIDAS_Y_CENAS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        Column {
            MyScrollableColumn(Modifier.weight(1f)) {
                Text("HOLA")
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = {
                    comidaStore.setShowAddComidaPopup(true)
                }) {
                    Text("Añadir comida")
                }
            }

            if (comidaStore.showAddComidaPopup.value) {
                AddComidaPopup()
            }
        }
    }
}