package org.ivandev.acomprar.screens.comida

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.models.Comida
import org.ivandev.acomprar.stores.ComidaStore

@Composable
fun AddComidaPopup() {
    val comidaStore: ComidaStore = viewModel(LocalContext.current as ViewModelStoreOwner)

    val showAddComidaPopup by remember { mutableStateOf(comidaStore.showAddComidaPopup) }
    var comidaNombre = remember { mutableStateOf("") }
    val comidaTipo = remember { mutableIntStateOf(comidaStore.tipoComidaToAdd.value) }

    if (showAddComidaPopup.value) {
        AlertDialog(
            onDismissRequest = { comidaStore.setShowAddComidaPopup(false) },
            confirmButton = {
                TextButton(onClick = {
                    comidaStore.addComida(
                        Comida(null, comidaNombre.value, comidaTipo.value)
                    )

                    comidaStore.setShowAddComidaPopup(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { comidaStore.setShowAddComidaPopup(false) }) { Text("Cancelar") }
            },
            title = { Text("Añadir comida") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NombreComidaTextField(comidaNombre)
                    Spacer(Tools.spacer8dpHeight)
                    TipoComidaDropdown(comidaTipo)
                }
            }
        )
    }
}

@Composable
fun NombreComidaTextField(comidaNombre: MutableState<String>) {
    TextField(
        value = comidaNombre.value,
        onValueChange = { comidaNombre.value = it },
        label = { Text("Nombre de la comida") }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TipoComidaDropdown(tipo: MutableState<Int>) {
    val expanded = remember { mutableStateOf(false) }
    val options: List<String> = TipoComidaEnum.getTiposString()
    val selectedOption = remember { mutableStateOf(options[tipo.value]) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        TextField(
            value = selectedOption.value,
            onValueChange = {},
            label = { Text("Tipo de comida") },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption.value = option
                        tipo.value = index
                        expanded.value = false
                    }
                )
            }
        }
    }
}
