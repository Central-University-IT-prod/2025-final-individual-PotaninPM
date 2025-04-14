package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.potaninpm.feature_home.R

@Composable
fun TickerSettingsDialog(
    currentInterval: Long,
    autoUpdateEnabled: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Long, Boolean) -> Unit
) {
    val context = LocalContext.current

    var intervalText by rememberSaveable { mutableStateOf(currentInterval.toString()) }
    var isError by rememberSaveable { mutableStateOf("") }
    var isAutoUpdateEnabled by rememberSaveable { mutableStateOf(autoUpdateEnabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.tickers_settings)) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Switch(
                        checked = isAutoUpdateEnabled,
                        onCheckedChange = { isAutoUpdateEnabled = it }
                    )
                    Text(stringResource(R.string.turn_on_auto_update), modifier = Modifier.padding(start = 8.dp))
                }

                if (isAutoUpdateEnabled) {
                    OutlinedTextField(
                        value = intervalText,
                        onValueChange = {
                            intervalText = it
                            isError = when {
                                it.isEmpty() -> context.getString(R.string.field_cant_be_empty)
                                (it.toIntOrNull() ?: 0) < 8 -> context.getString(R.string.interval_8)
                                (it.toIntOrNull() ?: 0) > 3600 -> context.getString(R.string.interval_3600)
                                else -> ""
                            }
                        },
                        label = { Text(stringResource(R.string.update_interval)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isError.isNotEmpty(),
                        supportingText = {
                            if (isError.isNotEmpty()) {
                                Text(text = isError)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = (isAutoUpdateEnabled && isError.isEmpty() && intervalText.isNotEmpty()) || autoUpdateEnabled,
                onClick = {
                    val newInterval = intervalText.toLongOrNull() ?: currentInterval
                    onConfirm(newInterval, isAutoUpdateEnabled)
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

