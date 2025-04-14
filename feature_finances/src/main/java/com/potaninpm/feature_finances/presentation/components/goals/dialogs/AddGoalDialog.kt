package com.potaninpm.feature_finances.presentation.components.goals.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.DatePicker
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onAddGoal: (String, Long, String, Long?) -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "add_goal_dialog_opened",
        properties = mapOf("add_goal_dialog" to "opened")
    )

    val context = LocalContext.current
    
    var name by rememberSaveable { mutableStateOf("") }
    var targetSum by rememberSaveable { mutableStateOf("") }
    val selectedCurrency by rememberSaveable { mutableStateOf("₽") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var targetSumError by rememberSaveable { mutableStateOf<String?>(null) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    if (showDatePicker) {
        DatePicker(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismissRequest = { showDatePicker = false }
        )
    }

    val isNameValid = name.isNotEmpty()
    val isTargetSumValid = targetSum.toLongOrNull()?.let { it <= 100_000_000 } ?: false
    val isFormValid = isNameValid && isTargetSumValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_goal)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomTextField(
                    value = name,
                    hint = stringResource(R.string.goal_name),
                    isError = nameError != null,
                    error = nameError,
                    onValueChange = {
                        name = it
                        nameError = if (it.isEmpty()) context.getString(R.string.title_cannot_be_empty) else null
                    }
                )

                CustomTextField(
                    value = targetSum,
                    hint = stringResource(R.string.target_sum),
                    type = "number",
                    isError = targetSumError != null,
                    error = targetSumError,
                    onValueChange = {
                        targetSum = it
                        targetSumError = when {
                            it.isEmpty() -> context.getString(R.string.sum_cannot_be_empty)
                            it.toLongOrNull() == null -> context.getString(R.string.enter_valid_figure)
                            it.toLong() > 100_000_000_000 -> context.getString(R.string.sum_cannot_be_more_that)
                            else -> null
                        }
                    }
                )

                CustomTextField(
                    value = selectedDate?.format(dateFormatter) ?: stringResource(R.string.choose_date),
                    hint = stringResource(R.string.approximate_time),
                    enabled = false,
                    isError = false,
                    error = null,
                    onValueChange = {},
                    onClick = {
                        showDatePicker = true
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetAmount = targetSum.toLongOrNull() ?: 0L
                    val dueDateTimestamp = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                    onAddGoal(name, targetAmount, selectedCurrency, dueDateTimestamp)
                },
                enabled = isFormValid
            ) {
                Text(stringResource(R.string.save), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


