package com.potaninpm.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    hint: String,
    type: String = "text",
    maxLines: Int = 1,
    enabled: Boolean = true,
    isError: Boolean = false,
    error: String? = null,
    onValueChange: (String) -> Unit,
    borderColor: Color? = null,
    onClick: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(hint) },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocused -> MaterialTheme.colorScheme.primary
                        borderColor != null -> borderColor
                        else -> Color.Gray
                    },
                    shape = MaterialTheme.shapes.medium
                ).clickable(
                    onClick = {
                        onClick()
                    }
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            enabled = enabled,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            isError = isError,
            keyboardOptions = if (type == "number") {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (isError && error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}