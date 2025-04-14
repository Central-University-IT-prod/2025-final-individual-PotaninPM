package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.ui.components.CustomElevatedCard

@Composable
fun SearchCategoriesCard(
    category: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    CustomElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        background = if (selected) MaterialTheme.colorScheme.onSurface else null,
        shape = CircleShape
    ) {
        Text(
            category,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
