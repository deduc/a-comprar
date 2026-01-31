package org.ivandev.acomprar.screens.comida

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.ConfirmationPopup
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.stores.ComidaStore

class ComidasScreen: Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.TextHomeNavigationButtons.COMIDAS_Y_CENAS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

        LaunchedEffect(Dispatchers.IO) {
            comidaStore.getAndSetAllComidasFromDb()
        }

        Column {
            MyScrollableColumn(Modifier.weight(1f)) {
                ComidasTable(comidaStore)
            }

            ButtonsPanel(comidaStore)

            PopupsAddDelete(comidaStore)
        }
    }

    @Composable
    fun ComidasTable(comidaStore: ComidaStore) {
        val comidasByTipo: SnapshotStateList<SnapshotStateList<ComidaEntity>> = comidaStore.getComidasFilteredByTipo()

        comidasByTipo.forEachIndexed { index: Int, listaTipoComidas: MutableList<ComidaEntity> ->
            val tipoComida: String = TipoComidaEnum.getTipoComidaPluralById(index)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(tipoComida, style = Tools.styleTitleBlack)

                Spacer(Tools.spacer8dpWidth)

                MyIcons.AddIcon {
                    comidaStore.setShowAddComidaPopup(true)
                    comidaStore.setTipoComidaToAdd(index)
                }
            }

            Spacer(Tools.spacer8dpHeight)

            if (listaTipoComidas.isNotEmpty()) {
                listaTipoComidas.forEach { comida: ComidaEntity ->
                    ComidaRowIteration(comida, comidaStore)
                }
            }
            else Text("No hay ${tipoComida}.")

            Spacer(Tools.spacer16dpHeight)
        }
    }

    @Composable
    fun ComidaRowIteration(comida: ComidaEntity, comidaStore: ComidaStore) {
        val navigator: Navigator = LocalNavigator.currentOrThrow

        Row(
            Modifier.fillMaxWidth().then(Tools.styleBorderBlack),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(Modifier.padding(Tools.padding8dp).weight(1f)) {
                Text(comida.nombre)
            }
            Column(Tools.styleBorderBlack.then(Modifier.padding(Tools.padding8dp))) {
                Row {
                    MyIcons.EditIcon {
//                        comidaStore.setShowAddOrEditComidaPopup(true)
//                        comidaStore.setComidaToEdit(comida)
                        navigator.push(EditComidaScreen(comida.id, comida.nombre, comida.tipo))
                    }
                    Spacer(Tools.spacer8dpWidth)
                    MyIcons.TrashIcon {
                        comidaStore.setShowDeleteComidaPopup(true)
                        comidaStore.setComidaToDelete(comida)
                    }
                }
            }
        }
    }

    @Composable
    fun ButtonsPanel(comidaStore: ComidaStore) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                comidaStore.setShowAddComidaPopup(true)
            }) {
                Text("Añadir comida")
            }
        }
    }

    @Composable
    private fun PopupsAddDelete(comidaStore: ComidaStore) {
        if (comidaStore.showAddComidaPopup.value) {
            AddComidaPopup()
        }

        if (comidaStore.showDeleteComidaPopup.value && comidaStore.comidaToDelete.value != null) {
            val comidaToDelete: ComidaEntity = comidaStore.comidaToDelete.value!!

            ConfirmationPopup(
                text = "¿Quieres borrar la comida ${comidaToDelete.nombre}?",
                onAcceptMethod = { comidaStore.deleteComidaById(comidaToDelete.id) },
                onDismiss = {
                    comidaStore.setShowDeleteComidaPopup(false)
                    comidaStore.setComidaToDelete(null)
                }
            )
        }
    }
}