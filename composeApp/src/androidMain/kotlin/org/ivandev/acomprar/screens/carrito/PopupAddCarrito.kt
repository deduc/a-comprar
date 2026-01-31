package org.ivandev.acomprar.screens.carrito

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.stores.CarritoStore

@Composable
fun PopupAddCarrito() {
    val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

    if (carritoStore.showAddCarritoPopup.value) {
        AlertDialog(
            onDismissRequest = { carritoStore.setShowAddCarritoPopup(false) },
            confirmButton = {
                TextButton(onClick = { carritoStore.addCarrito() }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { carritoStore.setShowAddCarritoPopup(false) }) {
                    Text("Cancelar")
                }
            },
            title = { Text(Literals.ButtonsText.ADD_CARRITO) },
            text = { AddCarritoContent(carritoStore) },
        )
    }
}

@Composable
fun AddCarritoContent(carritoStore: CarritoStore) {
    val carritoName = carritoStore._carritoName
    val carritoDescription = carritoStore._carritoDescription

    Column {
        TextField(
            value = carritoName.value,
            onValueChange = { carritoName.value = it },
            label = { Text("Nombre del carrito") }
        )

        Spacer(Tools.spacer24dpHeight)

        TextField(
            value = carritoDescription.value,
            onValueChange = { carritoDescription.value = it },
            label = { Text("Descripción del carrito") },
            modifier = Modifier.fillMaxWidth().height(180.dp),
            singleLine = false,
        )
    }
}