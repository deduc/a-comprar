package org.ivandev.acomprar.stores

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.screens.menu.classes.MyMenuComidas

class MenuStore : ViewModel() {
    val daysOfWeek: List<String> = Literals.DaysOfWeek.getDaysOfWeek()

    private var _menusList = mutableStateOf<List<MenuEntity>>(Database.getAllMenu())
    val menusList: State<List<MenuEntity>> = _menusList

    private var _checkedList = mutableStateListOf<MutableState<Boolean>>()
    val checkedList: SnapshotStateList<MutableState<Boolean>> = _checkedList

    private var _showAddMenuPopup = mutableStateOf<Boolean>(false)
    val showAddMenuPopup: State<Boolean> = _showAddMenuPopup

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMenus()
        }

        initializeCheckedList()
    }

    private fun initializeCheckedList() {
        _checkedList.clear()
        _checkedList.addAll(daysOfWeek.map { mutableStateOf(false) })
    }

    fun addMenuAndItsDays(menu: Menu): Boolean {
        if (!validateMenuAndItsDays(menu.nombre, checkedList)) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_VOID_DATA)
            return false
        }

        if (!addMenu(menu)) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        }

        val lastMenu: MenuEntity? = Database.getLastMenu()
        if (lastMenu == null) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        }

        if (!addMenuDays(lastMenu)) {
            Database.deleteLastMenu()
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU_DAYS)
            return false
        }

        Tools.Notifier.showToast(Literals.ToastText.ADDED_MENU)
        initializeCheckedList()

        return true
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


    private fun getAllMenus() {
        _menusList.value = Database.getAllMenu()
    }

    fun getComidasYCenasByMenuId(menuEntity: MenuEntity): MyMenuComidas {
        var myComidasYCenas: MyMenuComidas

        // Ejecutar la consulta en un hilo de fondo (IO) para no bloquear el hilo principal
        runBlocking(Dispatchers.IO) {
            val comidaEntities: List<ComidaEntity> = Database.getComidasByMenuId(menuEntity.id)
            myComidasYCenas = MyMenuComidas(menuEntity.id, menuEntity.nombre, comidaEntities)
        }

        return myComidasYCenas
    }

    fun toggleShowAddMenuPopup(newValue: Boolean) {
        _showAddMenuPopup.value = newValue
    }

    fun deleteCheckedData() {
        initializeCheckedList()
    }

    fun deleteMenu(menuEntity: MenuEntity) {
        val removed = Database.deleteMenu(menuEntity)
        if (removed) {
            _menusList.value = _menusList.value.filter { it.id != menuEntity.id }
        }
    }

    private fun validateMenuAndItsDays(menuName: String, checkedList: SnapshotStateList<MutableState<Boolean>>): Boolean {
        return menuName.isNotEmpty() && checkedList.any { it.value }
    }

    private fun addMenu(menu: Menu): Boolean {
        val added = Database.addMenu(menu)

        if (! added) {
            return false
        }
        else {
            val lastMenu = Database.getLastMenu()

            if (lastMenu != null) _menusList.value += lastMenu
            return true
        }
    }

    private fun addMenuDays(lastMenu: MenuEntity): Boolean {
        var result = false
        var menuDaysOfWeekEntity: MutableList<MenuDaysOfWeek> = mutableListOf()

        _checkedList.forEachIndexed {index: Int, selectedDay: State<Boolean> ->
            if (selectedDay.value) {
                menuDaysOfWeekEntity.add(
                    MenuDaysOfWeek(day = daysOfWeek[index])
                )
            }
        }

        if (Database.addMenuDays(lastMenu.id, menuDaysOfWeekEntity)) {
            result = true
        }


        return result
    }
}