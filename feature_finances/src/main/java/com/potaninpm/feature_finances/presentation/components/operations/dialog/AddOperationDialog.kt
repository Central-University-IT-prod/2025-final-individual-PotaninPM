package com.potaninpm.feature_finances.presentation.components.operations.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun AddOperationDialog(
    goals: List<GoalEntity>,
    onDismiss: () -> Unit,
    onAddOperation: (Long, Long, String?) -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "add_operation_dialog_opened",
        properties = mapOf("add_operation_dialog" to "opened")
    )

    val context = LocalContext.current

    var selectedGoal by remember { mutableStateOf(if (goals.isNotEmpty()) goals.first() else null) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }

    var amountError by rememberSaveable { mutableStateOf<String?>(null) }
    val maxAmount = selectedGoal?.targetAmount?.minus(selectedGoal?.currentAmount ?: 0L) ?: 0L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_operation)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (goals.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        CustomTextField(
                            value = goals.firstOrNull { it.id == selectedGoal?.id }?.title ?: "",
                            hint = stringResource(R.string.target),
                            enabled = false,
                            type = "text",
                            onValueChange = {

                            },
                            onClick = {
                                expanded = true
                            },
                            borderColor = MaterialTheme.colorScheme.primary
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            goals.forEach { goal ->
                                DropdownMenuItem(
                                    text = { Text(goal.title) },
                                    onClick = {
                                        selectedGoal = goal
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                CustomTextField(
                    value = amountText,
                    hint = stringResource(
                        R.string.operation_sum,
                        goals.firstOrNull { it.id == selectedGoal?.id }?.currency ?: "₽"
                    ),
                    type = "number",
                    isError = amountError != null,
                    error = amountError,
                    onValueChange = {
                        amountText = it

                        amountError = when {
                            it.isEmpty() -> context.getString(R.string.sum_cannot_be_empty)
                            it.toLongOrNull() == null -> context.getString(R.string.enter_valid_figure)
                            it.toLong() > maxAmount -> context.getString(
                                R.string.sum_is_bigger_than_goal, maxAmount.toString()
                            )
                            else -> null
                        }
                    }
                )

                CustomTextField(
                    value = comment,
                    hint = stringResource(R.string.comments_optional),
                    isError = false,
                    error = null,
                    onValueChange = { comment = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    val goal = selectedGoal ?: return@Button
                    onAddOperation(goal.id, amount, comment.takeIf { it.isNotBlank() })
                },
                enabled = amountError == null && amountText.isNotEmpty()
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
