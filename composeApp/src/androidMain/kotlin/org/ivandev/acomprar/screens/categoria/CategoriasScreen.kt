package org.ivandev.acomprar.screens.categoria

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.MyIcons
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.entities.CategoriaEntity
import org.ivandev.acomprar.stores.CategoriaStore

class CategoriasScreen : Screen {
    @Composable
    override fun Content() {
        CommonScreen(title = Literals.CATEGORIAS_TITLE) { MainContent() }.Render()
    }

    @Composable
    fun MainContent() {
        var showPopup by remember { mutableStateOf(false) }

        Column {
            Row(Modifier.weight(1f)) {
                MyScrollableColumn {
                    CategoriasContainer()
                }
            }

            // *** Barra inferior de botones ***
            Row(Modifier.fillMaxWidth().weight(0.125f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = { showPopup = true }) {
                    Text(Literals.ButtonsText.ADD_CATEGORIA)
                }
            }
        }

        if (showPopup) {
            AddCategoriaPopup()
        }
    }

    @Composable
    fun CategoriasContainer() {
        val navigator: Navigator = LocalNavigator.currentOrThrow
        val categoriaStore: CategoriaStore = viewModel()

        val categorias = categoriaStore.categorias
        var categoriaEntityToEdit: State<CategoriaEntity?> = categoriaStore.categoriaEntityToEdit
        var categoriaEntityToDelete: State<CategoriaEntity?> = categoriaStore.categoriaEntityToDelete

        Column(Tools.styleBorderBlack) {
            TableHeaders()

            // TABLE CONTENT
            categorias.value.forEachIndexed { index: Int, categoriaEntity: CategoriaEntity ->
                Row(Modifier.fillMaxWidth().border(1.dp, Color.Black), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

                    Column(Modifier.weight(0.65f).border(1.dp, Color.Black)) {
                        Text(categoriaEntity.nombre, Modifier.padding(Tools.padding8dp))
                    }

                    Column(Modifier.weight(0.35f).border(1.dp, Color.Black)) {
                        Row(Modifier.fillMaxWidth().padding(Tools.padding8dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            MyIcons.ViewIcon { seeCategoriaById(categoriaEntity, navigator) }

                            if(categoriaEntity.id != Literals.Database.ID_SIN_CATEGORIA_VALUE) {
                                MyIcons.EditIcon { categoriaStore.updateCategoriaToEdit(categoriaEntity) }
                                MyIcons.TrashIcon { categoriaStore.updateCategoriaToDelete(categoriaEntity) }
                            }
                        }
                    }
                }
            }
        }

        // Mostrar el popup si hay una categoría seleccionada
        if (categoriaEntityToEdit.value != null) {
            EditCategoriaPopup(categoriaEntityToEdit)
        }


        // Mostrar el popup si hay una categoría seleccionada
        if (categoriaEntityToDelete.value != null) {
            DeleteCategoriaPopup(categoriaEntityToDelete)
        }
    }

    @Composable
    fun TableHeaders() {
        Row(Modifier.fillMaxWidth().border(1.dp, Color.Black), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(0.65f).border(1.dp, Color.Black).padding(Tools.padding8dp)) {
                Text(Literals.Table.NOMBRE_COLUMN, style = Tools.styleTableHeader)
            }
            Column(Modifier.weight(0.35f).border(1.dp, Color.Black).padding(Tools.padding8dp)) {
                Text(Literals.Table.OPCIONES_COLUMN, style = Tools.styleTableHeader)
            }
        }
    }

    private fun seeCategoriaById(categoriaEntity: CategoriaEntity, navigator: Navigator) {
        navigator.push(SeeCategoriaAndProductsScreen(categoriaEntity.id, categoriaEntity.nombre))
    }
}
