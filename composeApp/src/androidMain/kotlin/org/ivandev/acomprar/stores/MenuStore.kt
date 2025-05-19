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
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.models.Menu
import org.ivandev.acomprar.models.MenuDaysOfWeek
import org.ivandev.acomprar.screens.comida.classes.ComidasYCenasSeparatedLists

class MenuStore : ViewModel() {
    val daysOfWeek: List<String> = Literals.DaysOfWeek.getDaysOfWeek()

    private var _menusList = mutableStateOf<List<MenuEntity>>(Database.getAllMenu())
    val menusList: State<List<MenuEntity>> = _menusList

    private var _checkedList = mutableStateListOf<MutableState<Boolean>>()
    val checkedList: SnapshotStateList<MutableState<Boolean>> = _checkedList

    private var _showAddMenuPopup = mutableStateOf<Boolean>(false)
    val showAddMenuPopup: State<Boolean> = _showAddMenuPopup

    private var _editingMenu = mutableStateOf<MenuEntity?>(null)
    val editingMenu: State<MenuEntity?> = _editingMenu

    private var _addedMenu = mutableStateOf<Boolean>(false)
    var addedMenu: State<Boolean> = _addedMenu

    private val _comidasYCenasByMenuId = mutableStateListOf<ComidaEntity?>()
    val comidasYCenasByMenuId: SnapshotStateList<ComidaEntity?> = _comidasYCenasByMenuId

    private val _addOrChangeProductoPopup = mutableStateOf<Boolean>(false)
    val addOrChangeProductoPopup: State<Boolean> = _addOrChangeProductoPopup

    private var _menuDaysOfWeekClicked = mutableStateOf<MenuDaysOfWeek?>(null)
    val menuDaysOfWeekClicked: State<MenuDaysOfWeek?> = _menuDaysOfWeekClicked

    var _menuDaysOfWeekList = mutableStateListOf<MenuDaysOfWeek>()
    val menuDaysOfWeekList: SnapshotStateList<MenuDaysOfWeek> = _menuDaysOfWeekList

    private var _comidasYCenasSeparatedLists = mutableStateOf<ComidasYCenasSeparatedLists>(ComidasYCenasSeparatedLists())
    val comidasYCenasSeparatedLists: State<ComidasYCenasSeparatedLists> = _comidasYCenasSeparatedLists

    var isComidaClickedAux = mutableStateOf(true)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMenus()
            getComidasYCenasSeparatedFromDB()
        }

        initializeCheckedList()
    }

    fun clearMenuDaysOfWeekList() {
        _menuDaysOfWeekList.clear()
    }

    fun onConfirmAddMenu(menu: Menu, checkedList: SnapshotStateList<MutableState<Boolean>>) {
        viewModelScope.launch {
            val added = addMenuAndItsDays(menu, checkedList)
            setShowAddMenuPopup(added)
        }
    }
    suspend fun addMenuAndItsDays(menu: Menu, checkedList: SnapshotStateList<MutableState<Boolean>>): Boolean {
        if (!validateMenuAndItsDays(menu.nombre, checkedList)) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_VOID_DATA)
            return false
        }

        val added = withContext(Dispatchers.IO) { Database.addMenu(menu) }
        if (!added) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        }

        val currentMenuAdded = withContext(Dispatchers.IO) { Database.getLastMenu() }
        if (currentMenuAdded == null) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        } else {
            _menusList.value += currentMenuAdded
        }

        val daysAdded = withContext(Dispatchers.IO) { addMenuDays(currentMenuAdded) }
        if (!daysAdded) {
            withContext(Dispatchers.IO) { Database.deleteLastMenu() }
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU_DAYS)
            return false
        }

        Tools.Notifier.showToast(Literals.ToastText.ADDED_MENU)
        initializeCheckedList()

        return true
    }

    fun deleteMenu(menuEntity: MenuEntity) {
        val removed = Database.deleteMenu(menuEntity)
        if (removed) {
            _menusList.value = _menusList.value.filter { it.id != menuEntity.id }
        }
    }

    fun deleteCheckedData() {
        initializeCheckedList()
    }

    fun getComidaById(idComida: Int?): ComidaEntity? {
        var comida: ComidaEntity? = null

        if (idComida != null) {
            comida = (_comidasYCenasSeparatedLists.value.comidas + _comidasYCenasSeparatedLists.value.cenas).find { it?.id == idComida }
        }

        return comida
    }

    fun getComidasYCenasSeparatedFromDB() {
        initializeComidasYCenasSeparatedLists()
        _comidasYCenasSeparatedLists.value.comidas = Database.getComidasByTipoId(TipoComidaEnum.COMIDA)
        _comidasYCenasSeparatedLists.value.cenas = Database.getComidasByTipoId(TipoComidaEnum.CENA)
    }

    suspend fun menuDaysOfWeekListAddAll(menuDaysOfWeekListAux: MutableList<MenuDaysOfWeek>) {
        withContext(Dispatchers.IO) {
            _menuDaysOfWeekList.clear()
            _menuDaysOfWeekList.addAll(menuDaysOfWeekListAux)
        }
    }

    fun setAddOrChangeProductoPopup(newValue: Boolean) {
        _addOrChangeProductoPopup.value = newValue
    }

    fun setEditingMenu(menuEntity: MenuEntity) {
        _editingMenu.value = menuEntity
    }

    fun setMenuDaysOfWeekClicked(menuDaysOfWeek: MenuDaysOfWeek?) {
        _menuDaysOfWeekClicked.value = menuDaysOfWeek
    }

    fun setShowAddMenuPopup(newValue: Boolean) {
        _showAddMenuPopup.value = !newValue
        setAddedMenu(!newValue)
    }

    fun setAddedMenu(newValue: Boolean) {
        _addedMenu.value = newValue
    }

    fun toggleShowAddMenuPopup(newValue: Boolean) {
        _showAddMenuPopup.value = newValue
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

    suspend fun getMenuDaysOfWeekByMenuId(menuId: Int): MutableList<MenuDaysOfWeek> {
        return withContext(Dispatchers.IO) {
            val data: MutableList<MenuDaysOfWeekEntity> = Database.getMenuDaysOfWeekByMenuId(menuId)
            val dataConverted = doConvertMenuDaysOfWeekToMutable(data)

            _menuDaysOfWeekList.clear()
            _menuDaysOfWeekList.addAll(dataConverted)

            menuDaysOfWeekList
        }
    }

    private fun doConvertMenuDaysOfWeekToMutable(data: MutableList<MenuDaysOfWeekEntity>): MutableList<MenuDaysOfWeek> {
        return data.map { MenuDaysOfWeek(it.id, it.idMenu, it.idComida, it.tipoComida, it.day) }.toMutableList()
    }

    private fun validateMenuAndItsDays(menuName: String, checkedList: SnapshotStateList<MutableState<Boolean>>): Boolean {
        return menuName.isNotEmpty() && checkedList.any { it.value }
    }

    private fun addMenuDays(currentMenuAdded: MenuEntity): Boolean {
        var result = false
        var menuDaysOfWeek: MutableList<MenuDaysOfWeek> = mutableListOf()

        _checkedList.forEachIndexed {index: Int, selectedDay: State<Boolean> ->
            if (selectedDay.value) {
                menuDaysOfWeek.add(
                    MenuDaysOfWeek(null, currentMenuAdded.id, null, null, daysOfWeek[index])
                )
            }
        }

        if (Database.addMenuDays(currentMenuAdded.id, menuDaysOfWeek)) {
            result = true
        }

        return result
    }

    private fun getAllMenus() {
        _menusList.value = Database.getAllMenu()
    }

    private fun initializeComidasYCenasSeparatedLists() {
        _comidasYCenasSeparatedLists.value.comidas = listOf(null)
        _comidasYCenasSeparatedLists.value.cenas = listOf(null)
    }

    private fun initializeCheckedList() {
        // all false by default
        _checkedList.clear()
        _checkedList.addAll(daysOfWeek.map { mutableStateOf(false) })

    }
}