package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Comida
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas

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

    fun addMenu(menu: Menu) {
        val added = Database.addMenu(menu)

        if (added) {
            val newMenu = Database.getLastMenu()
            _menus.value += newMenu
        }
    }

    fun getMenusList(): State<List<Menu>> {
        return menus
    }

    fun getComidasYCenasByMenuId(menu: Menu): MyMenuComidas {
        var myComidasYCenas: MyMenuComidas

        // Ejecutar la consulta en un hilo de fondo (IO) para no bloquear el hilo principal
        runBlocking(Dispatchers.IO) {
            val comidas: List<Comida> = Database.getComidasByMenuId(menu.id!!)
            myComidasYCenas = MyMenuComidas(menu.id, menu.nombre, comidas)
        }

        return myComidasYCenas
    }

    fun deleteMenu(menu: Menu) {
        val removed = Database.deleteMenu(menu)
        if (removed) {
            _menus.value = _menus.value.filter { it.id != menu.id }
        }
    }
}