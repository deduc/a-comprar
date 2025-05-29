package org.ivandev.acomprar.screens.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.screens.comida.classes.ComidasYCenasSeparatedLists
import org.ivandev.acomprar.screens.menu.classes.MyDropdownMenuData
import org.ivandev.acomprar.stores.MenuStore

@Composable
fun AddOrEditComidaInMenuPopup(menuDaysOfWeekEntity: MenuDaysOfWeek, menuStore: MenuStore) {
    val comidasYCenasSeparatedLists: State<ComidasYCenasSeparatedLists> = menuStore.comidasYCenasSeparatedLists
    val popupTitle = remember { mutableStateOf<String>("") }

    val myDropdownMenuDataComidas = MyDropdownMenuData(
        comidaOrCenaTitle = "Comidas",
        expanded = remember { mutableStateOf<Boolean>(false) },
        comidaSelected = remember { mutableStateOf<ComidaEntity?>(null) },
        isComida = true,
        comidasByTipo = comidasYCenasSeparatedLists.value.comidas
    )
    val myDropdownMenuDataCenas = MyDropdownMenuData(
        comidaOrCenaTitle = "Cenas",
        expanded = remember { mutableStateOf<Boolean>(false) },
        comidaSelected = remember { mutableStateOf<ComidaEntity?>(null) },
        isComida = false,
        comidasByTipo = comidasYCenasSeparatedLists.value.cenas
    )

    LaunchedEffect(Dispatchers.IO) {
        menuStore.getComidasYCenasSeparatedFromDB()
        popupTitle.value = getPopupTitle(menuDaysOfWeekEntity)
    }

    if (menuStore.addOrChangeProductoPopup.value) {
        AlertDialog(
            onDismissRequest = { onDismiss(menuStore) },
            confirmButton = {
                TextButton(onClick = { onClickFn(myDropdownMenuDataComidas, myDropdownMenuDataCenas, menuStore) }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(menuStore) }) {
                    Text("Cancelar")
                }
            },
            title = { Text(popupTitle.value) },
            text = {
                Column {
                    MyDropdownMenu(myDropdownMenuDataComidas, myDropdownMenuDataCenas.comidaSelected)
                    Spacer(Tools.spacer32dpHeight)
                    MyDropdownMenu(myDropdownMenuDataCenas, myDropdownMenuDataComidas.comidaSelected)
                }
            },
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

private fun onClickFn(
    menuDataComidas: MyDropdownMenuData,
    menuDataCenas: MyDropdownMenuData,
    menuStore: MenuStore
) {
    var updatedMenuDOW: MenuDaysOfWeek? = null
    val selected = menuDataComidas.comidaSelected.value ?: menuDataCenas.comidaSelected.value

    selected?.let { comida ->
        menuStore.menuDaysOfWeekClicked.value?.let { clicked ->
            updatedMenuDOW = menuStore._menuDaysOfWeekList.find { it.id == clicked.id }
            updatedMenuDOW?.idComida = comida.id
            clicked.idComida = comida.id
        }
    }

    if (updatedMenuDOW != null && updatedMenuDOW?.idComida != 0) {
        Database.updateMenuDaysOfWeekById(updatedMenuDOW!!)
    }
    else {
        menuStore.menuDaysOfWeekClicked.value!!.idComida = null
        Database.updateMenuDaysOfWeekById(menuStore.menuDaysOfWeekClicked.value!!)
    }

    menuStore.setAddOrChangeProductoPopup(false)
}

private fun getPopupTitle(menuDaysOfWeek: MenuDaysOfWeek): String {
    return "Seleccionar ${TipoComidaEnum.getTipoComidaById(menuDaysOfWeek.tipoComida!!)} del ${menuDaysOfWeek.day}."
}

private fun onDismiss(menuStore: MenuStore) {
    println("lalalalal")
    menuStore.setAddOrChangeProductoPopup(false)
//    menuStore.setAddOrChangeProductoPopup(false)
//    menuStore.setMenuDaysOfWeekClicked(null)
}