package org.ivandev.acomprar.screens.Pruebas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.ivandev.acomprar.components.MyScrollableColumn
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.models.MenuDaysOfWeek

class PruebasScreen: Screen {
    @Composable
    override fun Content() {
        MyScrollableColumn(Modifier.padding(8.dp)) {
            Text("Hola")
            BarraBusqueda()
            TablaMenuDaysOfWeek()

        }
    }

    @Composable
    fun BarraBusqueda() {
        var textFieldState: TextFieldState = rememberTextFieldState()
        var onSearch: (String) -> Unit = { println("Buscando...") }
        var searchResults: List<String> = listOf("Manzana", "Pera", "Pito")
        var modifier: Modifier = Modifier

        SimpleSearchBar(
            textFieldState,
            onSearch,
            searchResults,
            modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleSearchBar(
        textFieldState: TextFieldState,
        onSearch: (String) -> Unit,
        searchResults: List<String>,
        modifier: Modifier = Modifier
    ) {
        // Controls expansion state of the search bar
        var expanded = rememberSaveable { mutableStateOf(false) }

        Box(
            modifier
                .heightIn(max = 300.dp)
                .fillMaxSize()
                .semantics { isTraversalGroup = true }
        ) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textFieldState.text.toString(),
                        onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                        onSearch = {
                            onSearch(textFieldState.text.toString())
                            expanded.value = false
                        },
                        expanded = expanded.value,
                        onExpandedChange = { expanded.value = it },
                        placeholder = { Text("Search") }
                    )
                },
                expanded = expanded.value,
                onExpandedChange = { expanded.value = it },
            ) {
                // Display search results in a scrollable column
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    searchResults.forEach { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier
                                .clickable {
                                    textFieldState.edit { replace(0, length, result) }
                                    expanded.value = false
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TablaMenuDaysOfWeek() {
        var menuList = Database.getAllMenu()

        Column {
            Text("Menus -------")
            menuList.forEach { menu ->
                Column {
                    Text("${menu.id} - ${menu.nombre}")

                    val menuDOW = remember {
                        val menuDOWAux: MutableList<MenuDaysOfWeekEntity> = Database.getMenuDaysOfWeekByMenuId(3)
                        doMagic(menuDOWAux)
                    }

                    Column(Modifier.padding(8.dp)) {
                        Text("MenuDaysOfWeek -------")

                        Column(Modifier.fillMaxSize()) {
                            menuDOW.forEach { menuRow ->
                                Row(Modifier.fillMaxWidth()) {
                                    Column(Modifier.weight(1f)) {
                                        Text("${menuRow.id} - ${menuRow.idMenu} - ${menuRow.idComida.value} - ${menuRow.tipoComida} - ${menuRow.day}")
                                    }

                                    Button(onClick = {
                                        menuDOW.find { it.id == menuRow.id }!!.idComida.value = 47
                                    }) { Text("Cambiar") }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun doMagic(menuDOWAux: MutableList<MenuDaysOfWeekEntity>): SnapshotStateList<MenuDaysOfWeek> {
        val result = mutableStateListOf<MenuDaysOfWeek>()
        menuDOWAux.forEach {
            result.add(
                MenuDaysOfWeek(it.id, it.idMenu, it.idComida, it.tipoComida, it.day)
            )
        }
        return result
    }
}