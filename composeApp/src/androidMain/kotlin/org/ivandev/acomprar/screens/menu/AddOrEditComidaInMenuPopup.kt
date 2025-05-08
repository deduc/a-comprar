package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.stores.MenuStore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddOrEditComidaInMenuPopup(idMenu: Int, onDismiss: () -> Unit) {
    val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    val comidasYCenasByMenuId = menuStore.getComidasYCenasByMenuIdFormatted(idMenu)

    val expanded = remember { mutableStateOf<Boolean>(false) }
    val comidaSelected = remember { mutableStateOf<ComidaEntity?>(null) }
    val comidaSeleccionada = remember { mutableStateOf<ComidaEntity?>(null) }

    if (menuStore.addOrChangeProductoPopup.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(Literals.ButtonsText.CANEL_ACTION)
                }
            },
            text = {
                Column {
                    // Desplegable para seleccionar una categoría
                    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
                        TextField(
                            value = comidaSelected.value?.nombre ?: "Seleccionar categoría",
                            onValueChange = {},
                            label = { Text("Comida") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            comidasYCenasByMenuId.forEach { comida: ComidaEntity? ->
                                if (comida == null) {
                                    DropdownMenuItem(
                                        onClick = {
                                            comidaSeleccionada.value = null
                                            expanded.value = false
                                        },
                                        text = { Text("Sin valor") }
                                    )
                                }
                                else {
                                    DropdownMenuItem(
                                        onClick = {
                                            comidaSeleccionada.value = comida
                                            expanded.value = false
                                        },
                                        text = { comida.nombre }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}