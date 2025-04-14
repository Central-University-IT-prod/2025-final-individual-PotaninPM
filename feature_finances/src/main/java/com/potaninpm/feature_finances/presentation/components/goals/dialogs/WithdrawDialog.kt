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
fun WithdrawDialog(
    goal: GoalEntity,
    onDismiss: () -> Unit,
    onConfirm: (Long, String?) -> Unit,
    onDeleteGoal: () -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "withdraw_money_dialog_opened",
        properties = mapOf("withdraw_money_dialog" to "opened")
    )

    val context = LocalContext.current
    var amountText by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.withdraw_money)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    value = amountText,
                    hint = stringResource(R.string.sum_to_withdraw),
                    type = "number",
                    isError = errorText != null,
                    error = errorText,
                    onValueChange = { text ->
                        amountText = text

                        val amount = amountText.toLongOrNull() ?: 0L

                        errorText = when {
                            amount > goal.currentAmount -> {
                                context.getString(
                                    R.string.sum_is_bigger_than_balance,
                                    goal.currentAmount.toString(),
                                    goal.currency
                                )
                            }
                            !amountText.all { it.isDigit() } -> context.getString(R.string.enter_valid_figure)
                            amountText.isNotEmpty() && amountText[0] == '0' -> {
                                context.getString(R.string.enter_valid_figure)
                            }
                            else -> {
                                null
                            }
                        }
                    }
                )

                CustomTextField(
                    value = comment,
                    hint = stringResource(R.string.comments_optional),
                    onValueChange = { comment = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val amount = amountText.toLongOrNull() ?: 0L
                when {
                    amount > goal.currentAmount -> {
                        errorText = context.getString(
                            R.string.sum_is_bigger_than_balance,
                            goal.currentAmount.toString(),
                            goal.currency
                        )
                    }
                    amount == goal.currentAmount -> {
                        onDeleteGoal()
                    }
                    else -> {
                        onConfirm(amount, comment.takeIf { it.isNotBlank() })
                    }
                }
            }) {
                Text(stringResource(R.string.withdraw))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


