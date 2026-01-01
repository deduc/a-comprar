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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.stores.CarritoStore

@Composable
fun EditCarritoPopup() {
    val carritoStore: CarritoStore = viewModel(LocalContext.current as ViewModelStoreOwner)

    if (carritoStore.showEditCarritoPopup.value) {
        val carritoName = remember { mutableStateOf(carritoStore._carritoName.value) }
        val carritoDescription = remember { mutableStateOf(carritoStore._carritoDescription.value) }

        AlertDialog(
            onDismissRequest = { carritoStore.setShowEditCarritoPopup(false) },
            confirmButton = {
                TextButton(onClick = {
                    carritoStore._carritoName.value = carritoName.value
                    carritoStore._carritoDescription.value = carritoDescription.value
                    carritoStore.updateCarrito()
                    carritoStore.setShowEditCarritoPopup(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { carritoStore.setShowEditCarritoPopup(false) }) {
                    Text("Cancelar")
                }
            },
            title = { Text(Literals.ButtonsText.ADD_CARRITO) },
            text = { EditCarritoContent(carritoName, carritoDescription) },
        )
    }
}

@Composable
fun EditCarritoContent(
    carritoName: MutableState<String>,
    carritoDescription: MutableState<String>
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            singleLine = false,
        )
    }
}
