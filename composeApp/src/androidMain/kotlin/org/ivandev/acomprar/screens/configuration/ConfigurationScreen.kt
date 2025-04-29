package org.ivandev.acomprar.screens.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.components.CommonScreen
import org.ivandev.acomprar.components.ConfirmationPopup
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.models.Producto
import org.ivandev.acomprar.stores.ConfigurationStore

class ConfigurationScreen: Screen {
    @Composable
    override fun Content() {
        val screen = CommonScreen(
            title = Literals.CONFIGURATION_TITLE
        ) {
            MainContent()
        }

        screen.Render()
    }

    @Composable
    fun MainContent() {
        Column {
            TextSize()
            ImportJSON()
            DeleteAllData()
            AddTestData()
            DeleteAllProducts()
        }
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
    fun AddTestData() {
        val configurationStore: ConfigurationStore = viewModel()
        val coroutineScope = rememberCoroutineScope()

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Añadir datos de prueba", Modifier.weight(1f))
            ModifyButton({ configurationStore.setShowAddProductsPopup(true) })
        }

        if (configurationStore.showAddProductsPopup.value) {
            ConfirmationPopup(
                Literals.ButtonsText.ADD_TEST_DATA,
                onAcceptMethod = {
                    coroutineScope.launch(Dispatchers.IO) {
                        // sacar a Ok el resultado de un foreach del 0 al 8
                        val ok = (0..8).all {
                            val result = Database.addProductosList(
                                listOf(
                                    Producto(null, it, "aaa$it", "0 ud", ""),
                                    Producto(null, it, "abbb$it", null, null)
                                )
                            )
                            result
                        }

                        withContext(Dispatchers.Main) {
                            Tools.Notifier.showToast(if (ok) "Registros de prueba añadidos." else "Error al añadir registros de prueba.")
                        }
                    }
                },
                onDismiss = {
                    configurationStore.setShowAddProductsPopup(false)
                }
            )
        }
    }

    @Composable
    fun DeleteAllProducts() {
        val coroutineScope = rememberCoroutineScope()
        val configurationStore: ConfigurationStore = viewModel()

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Borrar todos los productos", modifier = Modifier.weight(1f))
            ModifyButton({ configurationStore.setShowDeleteDataPopup(true) })

            if (configurationStore.showDeleteDataPopup.value) {
                ConfirmationPopup(
                    Literals.ButtonsText.DELETE_ALL_PRODUCTS,
                    onAcceptMethod = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val ok = Database.deleteAllProducto()

                            withContext(Dispatchers.Main) {
                                Tools.Notifier.showToast(if (ok) "Productos borrados." else "Error inesperado mientras se borraban productos.")
                            }
                        }
                    },
                    onDismiss = { configurationStore.setShowDeleteDataPopup(false) }
                )
            }
        }
    }

    @Composable
    fun ModifyButton(
        onClickFun: () -> Unit = { println("Botón sin funcionalidad") },
        text: String = Literals.CHANGE_TEXT
    ) {
        Button(onClick = onClickFun) {
            Text(text)
        }
    }
}