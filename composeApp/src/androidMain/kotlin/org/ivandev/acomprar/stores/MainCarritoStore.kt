package org.ivandev.acomprar.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.Database

class MainCarritoStore: ViewModel() {
    fun addCarritoToMainCarrito(id: Int) {
        viewModelScope.launch {
            val added = withContext(Dispatchers.IO) {
                Database.addCarritoToMainCarrito(id)
            }

            if (added) Tools.Notifier.showToast(Literals.ToastText.ADDED_CARRITO_TO_MAIN_CARRITO)
            else Tools.Notifier.showToast(Literals.ToastText.ERROR_ADDING_CARRITO_TO_MAIN_CARRITO)
        }
    }
}