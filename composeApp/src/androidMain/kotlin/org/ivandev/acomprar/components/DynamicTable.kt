package org.ivandev.acomprar.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> DynamicTable(
    columnHeaders: List<String>,
    rowData: List<T>,
    cellContent: @Composable (T, String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        // Cabecera
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            columnHeaders.forEachIndexed { index, header ->
                Column(
                    Modifier
                        .weight(if (index == columnHeaders.lastIndex) 0.35f else 0.65f / (columnHeaders.size - 1)) // 30% para la última columna
                        .border(1.dp, Color.Black)
                        .fillMaxHeight()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(header, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                }
            }
        }

        // Filas dinámicas
        rowData.forEach { row ->
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                columnHeaders.forEachIndexed { index, column ->
                    Column(
                        Modifier
                            .weight(if (index == columnHeaders.lastIndex) 0.35f else 0.65f / (columnHeaders.size - 1))
                            .border(1.dp, Color.Black)
                            .fillMaxHeight()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        cellContent(row, column) // Extrae contenido dinámicamente
                    }
                }
            }
        }
    }
}
