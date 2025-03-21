package org.ivandev.acomprar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

object MyIcons {
    @Composable
    fun EditIcon(onClick: () -> Unit) {
        val editIcon: VectorPainter = rememberVectorPainter(Icons.Default.Edit)
        Icon(
            painter = editIcon,
            contentDescription = "Settings",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = Color.Black
        )
    }

    @Composable
    fun ViewIcon(onClick: () -> Unit) {
        val viewIcon: VectorPainter = rememberVectorPainter(Icons.Filled.Visibility)
        Icon(
            painter = viewIcon,
            contentDescription = "Settings",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = Color.Black
        )
    }

    @Composable
    fun TrashIcon(onClick: () -> Unit) {
        val viewIcon: VectorPainter = rememberVectorPainter(Icons.Filled.Delete)
        Icon(
            painter = viewIcon,
            contentDescription = "Settings",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = Color.Black
        )
    }
}