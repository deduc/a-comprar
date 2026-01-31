package org.ivandev.acomprar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ivandev.acomprar.Literals

@Composable
fun BigButtonIconText(
    text: String = Literals.APP_NAME,
    onClick: () -> Unit = {},
    buttonPaddingDp: Int = 16,
    containerVerticalPaddingDp: Int = 12,
    textSizeDp: Int = 22,
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = buttonPaddingDp.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = containerVerticalPaddingDp.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MyIcons.ShoppingCartIcon(tint = Color.White, onClick = {onClick()})
            Spacer(Modifier.width(12.dp))
            Text(text, fontSize = textSizeDp.sp, fontWeight = FontWeight.Bold)
        }
    }
}