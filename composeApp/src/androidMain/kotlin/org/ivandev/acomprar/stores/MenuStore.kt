package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.Comida
import org.ivandev.acomprar.database.entities.Menu
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.screens.menu.classes.MyComidasYCenas

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
            _menus.value += menu
        }
    }

    fun getMenusList(): State<List<Menu>> {
        return menus
    }

    fun getComidasYCenasByMenuId(menu: Menu): MyComidasYCenas {
        val comidas = Database.getComidasYCenasByMenuId(menu.id!!)
        val myComidasYCenas: MyComidasYCenas = transformComidas(menu, comidas)

        return myComidasYCenas
    }

    fun deleteMenu(menu: Menu) {
        val removed = Database.deleteMenu(menu)
        if (removed) {
            _menus.value = _menus.value.filter { it.id != menu.id }
        }
    }

    private fun transformComidas(menu: Menu, comidas: List<Comida>): MyComidasYCenas {
        val result = MyComidasYCenas(menu.id, menu.nombre, mutableListOf(), mutableListOf())

        comidas.forEach { comida: Comida ->
            if (comida.tipo == TipoComidaEnum.COMIDA) {
                result.comidas += comida
            }
            else if (comida.tipo == TipoComidaEnum.CENA) {
                result.cenas += comida
            }
        }

        return result
    }
}