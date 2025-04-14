package com.potaninpm.feature_finances.presentation.components.goals.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun TransferDialog(
    fromGoal: GoalEntity,
    availableTargetGoals: List<GoalEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long, Long, Long, String?) -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "transfer_money_dialog_opened",
        properties = mapOf("transfer_money_dialog" to "opened")
    )

    val context = LocalContext.current

    var selectedTarget by remember { mutableStateOf(if (availableTargetGoals.isNotEmpty()) availableTargetGoals.first() else null) }
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.transfer_money)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    value = fromGoal.title,
                    hint = stringResource(R.string.from_where_transfer),
                    type = "text",
                    enabled = false,
                    isError = false,
                    error = null,
                    onValueChange = {},
                    borderColor = null,
                    onClick = {}
                )

                Box {
                    CustomTextField(
                        value = selectedTarget?.title.orEmpty(),
                        hint = stringResource(R.string.where_transfer),
                        type = "text",
                        enabled = false,
                        isError = false,
                        error = null,
                        onValueChange = {},
                        borderColor = null,
                        onClick = { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        availableTargetGoals.forEach { targetGoal ->
                            DropdownMenuItem(
                                text = { Text(targetGoal.title) },
                                onClick = {
                                    selectedTarget = targetGoal
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                CustomTextField(
                    value = amountText,
                    hint = stringResource(R.string.transaction_sum),
                    type = "number",
                    enabled = true,
                    isError = amountError != "",
                    error = amountError,
                    onValueChange = { text ->
                        amountText = text
                        amountError = when {
                            text.isEmpty() -> context.getString(R.string.sum_cannot_be_empty)
                            !text.all { it.isDigit() } -> context.getString(R.string.enter_valid_figure)
                            text.toLong() > fromGoal.currentAmount -> context.getString(
                                R.string.not_enough_money,
                                fromGoal.currentAmount.toString()
                            )
                            text.toLong() > (selectedTarget?.targetAmount?.minus(selectedTarget?.currentAmount!!) ?: 0) -> context.getString(
                                R.string.not_enough_space,
                                (selectedTarget?.targetAmount?.minus(selectedTarget?.currentAmount!!) ?: 0).toString()
                            )
                            amountText.isNotEmpty() && amountText[0] == '0' -> {
                                context.getString(R.string.enter_valid_figure)
                            }
                            else -> ""
                        }
                    },
                    borderColor = null,
                    onClick = {}
                )

                CustomTextField(
                    value = comment,
                    hint = stringResource(R.string.comments_optional),
                    type = "text",
                    enabled = true,
                    isError = false,
                    error = null,
                    onValueChange = { comment = it },
                    borderColor = null,
                    onClick = {}
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    if (selectedTarget != null) {
                        onConfirm(fromGoal.id, selectedTarget!!.id, amount, comment.takeIf { it.isNotBlank() })
                    }
                },
                enabled = amountError == "" && amountText.isNotEmpty() && selectedTarget != null
            ) {
                Text(stringResource(R.string.transfer))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
