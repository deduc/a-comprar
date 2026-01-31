package org.ivandev.acomprar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ivandev.acomprar.Tools
import org.ivandev.acomprar.database.entities.CarritoEntity

@Composable
fun CarritoContainer(
    carrito: CarritoEntity,
    RightIcons: @Composable () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().then(Tools.styleBorderBlack),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.padding(8.dp).weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                carrito.getFixedName(),
                style = Tools.styleTitleBlack
            )

            Row(Modifier.padding(4.dp)) {
                Spacer(Modifier.width(8.dp))
                Text(carrito.getFixedDescription())
            }
        }
        RightIcons()
    }

    Spacer(Modifier.height(Tools.height16dp))
}