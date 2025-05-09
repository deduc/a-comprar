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
import org.ivandev.acomprar.database.entities.MenuDaysOfWeekEntity
import org.ivandev.acomprar.database.entities.MenuEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
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

    private var _editingMenu = mutableStateOf<MenuEntity?>(null)
    val editingMenu: State<MenuEntity?> = _editingMenu

    private val _comidasYCenasByMenuId = mutableStateListOf<ComidaEntity?>()
    val comidasYCenasByMenuId: SnapshotStateList<ComidaEntity?> = _comidasYCenasByMenuId

    private val _addOrChangeProductoPopup = mutableStateOf<Boolean>(false)
    val addOrChangeProductoPopup: State<Boolean> = _addOrChangeProductoPopup

    private val _addOrChangeComida = mutableStateOf<Boolean>(false)
    val addOrChangeComida: State<Boolean> = _addOrChangeComida

    private val _addOrChangeCena = mutableStateOf<Boolean>(false)
    val addOrChangeCena: State<Boolean> = _addOrChangeCena


    init {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMenus()
        }

        initializeCheckedList()
    }

    fun addMenuAndItsDays(menu: Menu, checkedList: SnapshotStateList<MutableState<Boolean>>): Boolean {
        if (!validateMenuAndItsDays(menu.nombre, checkedList)) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_VOID_DATA)
            return false
        }

        if (!addMenu(menu)) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        }

        val currentMenuAdded: MenuEntity? = Database.getLastMenu()
        if (currentMenuAdded == null) {
            Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_MENU)
            return false
        }
        else _menusList.value += currentMenuAdded

        if (!addMenuDays(currentMenuAdded)) {
            Database.deleteLastMenu()
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

    fun getComidasYCenasByMenuId(menuEntity: MenuEntity): MyMenuComidas {
        var myComidasYCenas: MyMenuComidas

        // Ejecutar la consulta en un hilo de fondo (IO) para no bloquear el hilo principal
        runBlocking(Dispatchers.IO) {
            val comidaEntities: List<ComidaEntity> = Database.getComidasByTipoId(TipoComidaEnum.COMIDA)
            myComidasYCenas = MyMenuComidas(menuEntity.id, menuEntity.nombre, comidaEntities)
        }

        return myComidasYCenas
    }

    fun getComidasYCenasByMenuIdFormatted(idMenu: Int): List<ComidaEntity?> {
        val comidasYCenas = mutableListOf<ComidaEntity?>()
        comidasYCenas.add(null)

        val comidasList = Database.getComidasByTipoId(TipoComidaEnum.COMIDA)
        val cenasList = Database.getComidasByTipoId(TipoComidaEnum.CENA)

        (comidasList + cenasList).forEach { comida ->
            comidasYCenas.add(ComidaEntity(comida.id, comida.nombre, comida.tipo))
        }

        return comidasYCenas
    }

    fun getComidaById(idComida: Int?): ComidaEntity? {
        return if (idComida != null)
            _comidasYCenasByMenuId.find { it?.id == idComida && it.tipo ==  TipoComidaEnum.COMIDA }
        else
            null
    }

    fun getCenaById(idCena: Int?): ComidaEntity? {
        return if (idCena != null)
            _comidasYCenasByMenuId.find { it?.id == idCena && it.tipo ==  TipoComidaEnum.CENA }
        else
            null
    }

    fun setAddOrChangeComida(newValue: Boolean) {
        _addOrChangeComida.value = newValue
    }

    fun setAddOrChangeCena(newValue: Boolean) {
        _addOrChangeCena.value = newValue
    }

    fun setAddOrChangeProductoPopup(newValue: Boolean) {
        _addOrChangeProductoPopup.value = newValue
    }

    fun setEditingMenu(menuEntity: MenuEntity) {
        _editingMenu.value = menuEntity
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

    suspend fun getMenuDaysOfWeekByMenuId(menuId: Int): MutableList<MenuDaysOfWeekEntity> {
        return withContext(Dispatchers.IO) {
            Database.getMenuDaysOfWeekByMenuId(menuId)
        }
    }

    private fun validateMenuAndItsDays(menuName: String, checkedList: SnapshotStateList<MutableState<Boolean>>): Boolean {
        return menuName.isNotEmpty() && checkedList.any { it.value }
    }

    private fun addMenu(menu: Menu): Boolean {
        return Database.addMenu(menu)
    }

    private fun addMenuDays(currentMenuAdded: MenuEntity): Boolean {
        var result = false
        var menuDaysOfWeek: MutableList<MenuDaysOfWeek> = mutableListOf()

        _checkedList.forEachIndexed {index: Int, selectedDay: State<Boolean> ->
            if (selectedDay.value) {
                menuDaysOfWeek.add(
                    MenuDaysOfWeek(day = daysOfWeek[index], idMenu = currentMenuAdded.id)
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

    private fun initializeCheckedList() {
        _checkedList.clear()
        _checkedList.addAll(daysOfWeek.map { mutableStateOf(false) })
    }
}