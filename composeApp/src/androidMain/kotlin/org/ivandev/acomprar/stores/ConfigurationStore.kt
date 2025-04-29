package org.ivandev.acomprar.stores

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ConfigurationStore : ViewModel() {
    private val _showAddProductsPopup = mutableStateOf(false)
    val showAddProductsPopup: State<Boolean> = _showAddProductsPopup

    private val _showDeleteDataPopup = mutableStateOf(false)
    val showDeleteDataPopup: State<Boolean> = _showDeleteDataPopup

    fun setShowAddProductsPopup(value: Boolean) {
        _showAddProductsPopup.value = value
    }

    fun setShowDeleteDataPopup(value: Boolean) {
        _showDeleteDataPopup.value = value
    }
}
