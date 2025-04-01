package org.ivandev.acomprar.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MySimpleTable(headers: List<String>, rowData: List<Map<String, String>>) {
    MyScrollableColumn {
        // Headers
        Row(Modifier.fillMaxWidth().border(1.dp, Color.Black)) {
            headers.forEach { header: String ->
                Text(
                    text = header,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .heightIn(min = 48.dp), // Asegura un tamaño mínimo para las celdas
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Data
        rowData.forEach { row: Map<String, String> ->
            Row(Modifier.fillMaxWidth().border(1.dp, Color.Black)) {
                row.forEach { (key, value) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .heightIn(min = 48.dp) // Asegura un tamaño mínimo para las celdas
                    ) {
                        Text(
                            text = value.ifEmpty { " " }, // Si está vacío, muestra un espacio para que ocupe su espacio
                            style = MaterialTheme.typography.body2,
                            maxLines = Int.MAX_VALUE,  // Permite que el texto se envuelva
                            overflow = TextOverflow.Ellipsis,  // Si es muy largo, se corta
                            softWrap = true, // Permite que el texto se ajuste a la siguiente línea
                            modifier = Modifier.fillMaxSize()  // Asegura que el texto ocupe el espacio de la celda
                        )
                    }
                }
            }
        }
    }
}
