package org.ivandev.acomprar.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

object MyIcons {
    @Composable
    fun AddIcon(
        modifier: Modifier = Modifier,
        tint: Color = Color.Black,
        onClick: () -> Unit
    ) {
        val icon: VectorPainter = rememberVectorPainter(Icons.Filled.Add)
        Icon(
            painter = icon,
            contentDescription = "Add",
            modifier = modifier.then(
                borderCircle()
            ).clickable { onClick() },
            tint = tint
        )
    }

    /**
     * Icono de restar -
     */
    @Composable
    fun RemoveIcon(onClick: () -> Unit) {
        val icon: VectorPainter = rememberVectorPainter(Icons.Default.Remove)
        Icon(
            painter = icon,
            contentDescription = "Delete",
            modifier = Modifier.size(24.dp).then(
                borderCircle()
            ).clickable { onClick() },
            tint = Color.Black
        )
    }

    @Composable
    fun EditIcon(
        modifier: Modifier = Modifier,
        tint: Color = Color.Black,
        onClick: () -> Unit) 
    {
        val icon: VectorPainter = rememberVectorPainter(Icons.Default.Edit)
        Icon(
            painter = icon,
            contentDescription = "Edit",
            modifier = Modifier.size(24.dp).clickable { onClick() }.then(modifier),
            tint = tint
        )
    }

    @Composable
    fun ViewIcon(onClick: () -> Unit) {
        val icon: VectorPainter = rememberVectorPainter(Icons.Filled.Visibility)
        Icon(
            painter = icon,
            contentDescription = "View",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = Color.Black
        )
    }

    @Composable
    fun TrashIcon(
        tint: Color = Color.Black,
        onClick: () -> Unit) 
    {
        val icon: VectorPainter = rememberVectorPainter(Icons.Filled.Delete)
        Icon(
            painter = icon,
            contentDescription = "Trash",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = tint
        )
    }

    @Composable
    fun SaveIcon(onClick: () -> Unit) {
        val icon: VectorPainter = rememberVectorPainter(Icons.Default.Save)
        Icon(
            painter = icon,
            contentDescription = "Save",
            modifier = Modifier.size(24.dp).clickable { onClick() },
            tint = Color.Black
        )
    }

    fun borderCircle(): Modifier {
        return Modifier.border(1.dp, Color.Black, CircleShape)
    }
}