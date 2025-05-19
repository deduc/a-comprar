package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.screens.comida.classes.ComidasYCenasSeparatedLists
import org.ivandev.acomprar.screens.menu.classes.MyDropdownMenuData
import org.ivandev.acomprar.stores.MenuStore

@Composable
fun AddOrEditComidaInMenuPopup(menuDaysOfWeekEntity: MenuDaysOfWeek, onDismiss: () -> Unit) {
    val menuStore: MenuStore = viewModel(LocalContext.current as ViewModelStoreOwner)
    menuStore.getComidasYCenasSeparatedFromDB()

    val comidasYCenasSeparatedLists: State<ComidasYCenasSeparatedLists> = menuStore.comidasYCenasSeparatedLists
    var comidasYCenasFullList: SnapshotStateList<ComidaEntity?> = remember { mutableStateListOf() }

    val popupTitle: String = getPopupTitle(menuDaysOfWeekEntity)

    val expandedComida = remember { mutableStateOf<Boolean>(false) }
    val expandedCena = remember { mutableStateOf<Boolean>(false) }
    val comidaSelected = remember { mutableStateOf<ComidaEntity?>(null) }
    val cenaSelected = remember { mutableStateOf<ComidaEntity?>(null) }

    val myDropdownMenuDataComidas = MyDropdownMenuData(
        "Comidas",
        expandedComida,
        comidaSelected,
        true,
        comidasYCenasSeparatedLists.value.comidas
    )
    val myDropdownMenuDataCenas = MyDropdownMenuData(
        "Cenas",
        expandedCena,
        cenaSelected,
        false,
        comidasYCenasSeparatedLists.value.cenas
    )

    LaunchedEffect(comidasYCenasSeparatedLists) {
        comidasYCenasFullList.clear()
        comidasYCenasFullList.addAll(comidasYCenasSeparatedLists.value.comidas)
        comidasYCenasFullList.addAll(comidasYCenasSeparatedLists.value.cenas)
    }

    if (menuStore.addOrChangeProductoPopup.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        menuStore.menuDaysOfWeekClicked
                        menuStore.setAddOrChangeProductoPopup(false)
                        menuStore._menuDaysOfWeekList[0].idComida.value = 1
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
            title = { Text(popupTitle) },
            text = {
                Column {
                    Text("${comidaSelected.value?.id}, ${comidaSelected.value?.nombre}, ${comidaSelected.value?.tipo}")
                    Text("${cenaSelected.value?.id}, ${cenaSelected.value?.nombre}, ${cenaSelected.value?.tipo}")

                    MyDropdownMenu(myDropdownMenuDataComidas, cenaSelected)
                    Spacer(Tools.spacer32dpHeight)
                    MyDropdownMenu(myDropdownMenuDataCenas, comidaSelected)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyDropdownMenu(data: MyDropdownMenuData, otherComidaOrCenaSelected: MutableState<ComidaEntity?>) {
    Column {
        Text(data.comidaOrCenaTitle)

        Spacer(Tools.spacer8dpHeight)

        ExposedDropdownMenuBox(expanded = data.expanded.value, onExpandedChange = { data.expanded.value = !data.expanded.value }) {
            TextField(
                value = data.comidaSelected.value?.nombre ?: "Seleccionar ${data.comidaOrCenaTitle}",
                onValueChange = { it: String ->
                    data.comidaSelected.value?.nombre = it
                },
                label = { Text(data.comidaOrCenaTitle) },
                readOnly = true,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )

            ExposedDropdownMenu(
                expanded = data.expanded.value,
                onDismissRequest = { data.expanded.value = false }
            ) {
                data.comidasByTipo.forEach { comida: ComidaEntity? ->
                    if (comida == null) {
                        DropdownMenuItem(
                            onClick = {
                                data.comidaSelected.value = null
                                data.expanded.value = false
                            },
                            text = { Text("Sin valor") }
                        )
                    }
                    else {
                        DropdownMenuItem(
                            onClick = {
                                data.comidaSelected.value = comida
                                data.expanded.value = false
                                otherComidaOrCenaSelected.value = null
                            },
                            text = { Text(comida.nombre) }
                        )
                    }
                }
            }
        }
    }
}

private fun getPopupTitle(menuDaysOfWeek: MenuDaysOfWeek): String {
    return "Seleccionar ${TipoComidaEnum.getTipoComidaById(menuDaysOfWeek.tipoComida!!)} del ${menuDaysOfWeek.day}."
}