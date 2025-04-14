package com.potaninpm.feature_finances.presentation.components.goals.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.CustomTextField
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity

@Composable
fun DepositMoneyDialog(
    goal: GoalEntity,
    onDismiss: () -> Unit,
    onConfirm: (GoalEntity, Long, String?) -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "deposit_money_dialog_opened",
        properties = mapOf("deposit_money_dialog" to "opened")
    )

    val context = LocalContext.current

    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }

    var amountError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_money_to_goal)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    value = goal.title,
                    hint = stringResource(R.string.where_to_deposit),
                    type = "text",
                    enabled = false,
                    isError = false,
                    error = null,
                    onValueChange = {},
                    borderColor = null,
                    onClick = {}
                )

                CustomTextField(
                    value = amountText,
                    hint = stringResource(R.string.deposit_amount),
                    type = "number",
                    enabled = true,
                    isError = amountError != "",
                    error = amountError,
                    onValueChange = { text ->
                        amountText = text
                        amountError = when {
                            text.isEmpty() -> context.getString(R.string.sum_cannot_be_empty)
                            !text.all { it.isDigit() } || text[0] == '0' -> context.getString(R.string.enter_valid_figure)
                            amountText.toLong() > (goal.targetAmount - goal.currentAmount) -> {
                                context.getString(
                                    R.string.sum_is_bigger_than_balance,
                                    (goal.targetAmount - goal.currentAmount).toString(),
                                    goal.currency
                                )
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
                    onConfirm(goal, amount, comment.trim().takeIf { it.isNotBlank() })
                },
                enabled = amountError == "" && amountText.isNotEmpty()
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