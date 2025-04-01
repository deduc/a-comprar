package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Menu

class MenuStore : ViewModel() {
    // valor modificable
    private val _menus = mutableStateOf<List<Menu>>(Database.getAllMenu())
    // valor para obtener
    val menus: State<List<Menu>> = _menus

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _menus.value = Database.getAllMenu()
        }
    }
}