package com.potaninpm.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddButton(
    enabled: Boolean = true,
    onAddClick: () -> Unit,
    @StringRes title: Int
) {
    val iconTintColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray
    val textColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray

    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable {
                if (enabled) {
                    onAddClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp),
            tint = iconTintColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            stringResource(title),
            fontSize = 17.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}