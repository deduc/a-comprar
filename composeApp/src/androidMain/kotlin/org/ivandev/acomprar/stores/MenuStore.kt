package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas

class MenuStore : ViewModel() {
    // valor modificable
    private val _menus = mutableStateOf<List<MenuEntity>>(Database.getAllMenu())
    // valor para obtener
    val menus: State<List<MenuEntity>> = _menus

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

    fun getMenusList(): State<List<MenuEntity>> {
        return menus
    }

    fun getComidasYCenasByMenuId(menuEntity: MenuEntity): MyMenuComidas {
        var myComidasYCenas: MyMenuComidas

        // Ejecutar la consulta en un hilo de fondo (IO) para no bloquear el hilo principal
        runBlocking(Dispatchers.IO) {
            val comidaEntities: List<ComidaEntity> = Database.getComidasByMenuId(menuEntity.id!!)
            myComidasYCenas = MyMenuComidas(menuEntity.id, menuEntity.nombre, comidaEntities)
        }

        return myComidasYCenas
    }

    fun deleteMenu(menuEntity: MenuEntity) {
        val removed = Database.deleteMenu(menuEntity)
        if (removed) {
            _menus.value = _menus.value.filter { it.id != menuEntity.id }
        }
    }
}