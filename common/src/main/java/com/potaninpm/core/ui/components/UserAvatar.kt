package com.potaninpm.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UserAvatar(
    username: String,
    size: Int = 36
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(Color(0xFFD1E3FF), shape = CircleShape)
    ) {
        Text(
            text = username.firstOrNull()?.toString().orEmpty(),
            modifier = Modifier.align(Alignment.Center),
            color = Color(0xFF3B5998),
            fontWeight = FontWeight.Bold
        )
    }
}

