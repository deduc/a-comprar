package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.database.Database
import org.ivandev.acomprar.database.entities.ComidaEntity
import org.ivandev.acomprar.enumeration.TipoComidaEnum
import org.ivandev.acomprar.models.Comida

class ComidaStore: ViewModel() {
    private var _comidasList = mutableStateListOf<ComidaEntity>()
    val comidasList: SnapshotStateList<ComidaEntity> = _comidasList

    private var _comidasByTipo = mutableStateListOf<SnapshotStateList<ComidaEntity>>()
    val comidasByTipo: SnapshotStateList<SnapshotStateList<ComidaEntity>> get() = _comidasByTipo

    private var _comidaToEdit = mutableStateOf<ComidaEntity?>(null)
    val comidaToEdit: State<ComidaEntity?> = _comidaToEdit

    private var _comidaToDelete = mutableStateOf<ComidaEntity?>(null)
    val comidaToDelete: State<ComidaEntity?> = _comidaToDelete

    private var _tipoComidaToAdd = mutableStateOf<Int>(TipoComidaEnum.COMIDA)
    val tipoComidaToAdd: State<Int> = _tipoComidaToAdd

    private var _showAddComidaPopup = mutableStateOf<Boolean>(false)
    val showAddComidaPopup: State<Boolean> = _showAddComidaPopup

    private var _showDeleteComidaPopup = mutableStateOf<Boolean>(false)
    val showDeleteComidaPopup: State<Boolean> = _showDeleteComidaPopup

    fun addComida(comida: Comida): Boolean {
        var result = false
        val added = Database.addComida(comida)

        if (added != null) {
            _comidasList.add(added)
            result = true
        }
        else {
            println("Error de algun tipo")
        }

        return result
    }

    fun clearComidaToEdit() {
        _comidaToEdit.value = null
    }

    fun updateComida(comida: ComidaEntity): Boolean {
        if (!Database.updateComidaById(comida)) {
            println("Error de algún tipo")
            return false
        }

        val index = _comidasList.indexOfFirst { it.id == comida.id }
        if (index != -1) {
            _comidasList[index] = comida.copy() // sustituimos el objeto: Compose se entera y actualiza datos
        }

        _comidasByTipo = getComidasFilteredByTipo()

        return true
    }

    fun updateComidaNombreById(comida: ComidaEntity): Boolean {
        return Database.updateComidaById(comida)
    }

    fun setTipoComidaToAdd(newValue: Int) {
        _tipoComidaToAdd.value = newValue
    }

    fun setShowAddComidaPopup(newValue: Boolean) {
        _showAddComidaPopup.value = newValue
    }

    fun setShowDeleteComidaPopup(newValue: Boolean) {
        _showDeleteComidaPopup.value = newValue
    }

    fun setComidaToEdit(comida: ComidaEntity) {
        _comidaToEdit.value = comida
    }

    fun setComidaToDelete(comida: ComidaEntity?) {
        _comidaToDelete.value = comida
    }

    fun getAndSetAllComidasFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val comidasAux = Database.getAllComidas()

            withContext(Dispatchers.Main) {
                _comidasList.clear()
                _comidasList.addAll(comidasAux)
            }
        }
    }

    fun getComidasFilteredByTipo(): SnapshotStateList<SnapshotStateList<ComidaEntity>> {
        var comidasByTipoAux = TipoComidaEnum.getComidasFilteredByTipo(_comidasList)
        _comidasByTipo = comidasByTipoAux

        return comidasByTipo
    }

    fun getComidaToEditValues(): State<ComidaEntity?> {
        if (comidaToEdit.value != null) {
            return mutableStateOf(
                ComidaEntity(comidaToEdit.value!!.id, comidaToEdit.value!!.nombre, comidaToEdit.value!!.tipo)
            )
        }
        else {
            return mutableStateOf(null)
        }
    }

    fun deleteComidaById(comidaId: Int): Boolean {
        var result = false
        val deleted = Database.deleteComidaById(comidaId)

        if (deleted) {
            _comidasList.removeAll { it.id == comidaId }
        }

        return result
    }
}