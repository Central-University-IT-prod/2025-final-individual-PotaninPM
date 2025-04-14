package com.potaninpm.feature_finances.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.res.stringResource
import com.potaninpm.feature_finances.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { selectedMillis ->
                    val localDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(localDate)
                }
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
