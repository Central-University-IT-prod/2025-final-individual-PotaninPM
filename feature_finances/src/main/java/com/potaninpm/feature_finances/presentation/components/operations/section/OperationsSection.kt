package com.potaninpm.feature_finances.presentation.components.operations.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.AddButton
import com.potaninpm.feature_finances.domain.Operation
import com.potaninpm.feature_finances.presentation.screens.groupOperationsByDate
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.domain.OperationType

@Composable
fun OperationsSection(
    goalQuantity: Int,
    operations: List<Operation>,
    onAddOperationClick: () -> Unit
) {
    val grouped = groupOperationsByDate(operations)
        .toList()
        .sortedByDescending { it.first }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AddButton(
            enabled = goalQuantity > 0,
            onAddClick = {
                AnalyticsManager.logEvent(
                    eventName = "add_operation_btn_clicked",
                    properties = mapOf("add_operation_btn" to "clicked")
                )

                onAddOperationClick()
            },
            title = R.string.add_operation
        )

        if (grouped.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .alpha(0.7f),
                text = stringResource(R.string.no_operations_yet),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            grouped.forEach { (date, ops) ->
                DateHeader(date = date)

                ops.forEach { operation ->
                    OperationItem(
                        operation = operation,
                        icon = {
                            when (operation.type) {
                                OperationType.DEPOSIT.type -> {
                                    Icon(
                                        painter = painterResource(R.drawable.add_24px),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(8.dp)
                                    )
                                }
                                OperationType.WITHDRAWAL.type -> {
                                    Icon(
                                        painter = painterResource(R.drawable.remove_24px),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(8.dp)
                                    )
                                }
                                OperationType.TRANSFER.type -> {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(8.dp)
                                    )
                                }
                                OperationType.DELETE.type -> {
                                    Icon(
                                        painter = painterResource(R.drawable.delete_24px),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
