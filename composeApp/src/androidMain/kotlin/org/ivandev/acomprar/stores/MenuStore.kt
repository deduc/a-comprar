package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas

class MenuStore : ViewModel() {
    private val _menusList = mutableStateOf<List<MenuEntity>>(Database.getAllMenu())
    val menusList: State<List<MenuEntity>> = _menusList


    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMenus()
        }
    }

    fun addMenu(menu: Menu) {
        val added = Database.addMenu(menu)

        if (added) {
            val newMenu = Database.getLastMenu()
            _menusList.value += newMenu
        }
    }

    fun updateMenuNameById(menu: MenuEntity, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val updated = withContext(Dispatchers.IO) {
                Database.updateMenuNameById(menu)
            }

            if (updated) getAllMenus()

            onComplete(updated)
        }
    }


    fun getAllMenus() {
        _menusList.value = Database.getAllMenu()
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
            _menusList.value = _menusList.value.filter { it.id != menuEntity.id }
        }
    }
}