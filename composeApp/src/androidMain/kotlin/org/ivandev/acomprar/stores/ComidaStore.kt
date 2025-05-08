package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ComidaStore: ViewModel() {
    private var _showAddComidaPopup = mutableStateOf<Boolean>(false)
    val showAddComidaPopup: State<Boolean> = _showAddComidaPopup

    fun setShowAddComidaPopup(newValue: Boolean) {
        _showAddComidaPopup.value = newValue
    }
}