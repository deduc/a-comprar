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
import org.ivandev.acomprar.models.Comida

class ComidaStore: ViewModel() {
    private var _comidasList = mutableStateListOf<ComidaEntity>()
    val comidasList: SnapshotStateList<ComidaEntity> = _comidasList

    private var _showAddComidaPopup = mutableStateOf<Boolean>(false)
    val showAddComidaPopup: State<Boolean> = _showAddComidaPopup

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

    fun setShowAddComidaPopup(newValue: Boolean) {
        _showAddComidaPopup.value = newValue
    }

    fun getAllComidasFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val comidasAux = Database.getAllComidas()

            withContext(Dispatchers.Main) {
                _comidasList.clear()
                _comidasList.addAll(comidasAux)
            }
        }
    }

    fun deleteComidaById(comidaId: Int): Boolean {
        return Database.deleteComidaById(comidaId)
    }
}