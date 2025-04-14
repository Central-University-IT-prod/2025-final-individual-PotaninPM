package com.potaninpm.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun CustomElevatedCard(
    modifier: Modifier,
    background: Color?,
    shape: Shape = CardDefaults.shape,
    content: @Composable () -> Unit
) {
    val backgroundColor = background ?: if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        MaterialTheme.colorScheme.background
    }

    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults
            .elevatedCardColors(
                containerColor = backgroundColor
            ),
        shape = shape
    ) {
        content()
    }
}

