package com.potaninpm.feature_finances.presentation.components.operations.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.potaninpm.core.functions.formatMoneySigned
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.domain.Operation
import com.potaninpm.feature_finances.domain.OperationType
import com.potaninpm.feature_finances.presentation.components.operations.CommentSection

@Composable
fun OperationItem(
    operation: Operation,
    icon: @Composable () -> Unit
) {
    val operationType = when (operation.type) {
        OperationType.DEPOSIT.type -> stringResource(R.string.income)
        OperationType.WITHDRAWAL.type -> stringResource(R.string.withdrawal)
        OperationType.TRANSFER.type -> stringResource(R.string.transfer)
        OperationType.DELETE.type -> stringResource(R.string.delete)
        else -> ""
    }

    val iconColor = when (operation.type) {
        OperationType.DEPOSIT.type -> MaterialTheme.colorScheme.primary
        OperationType.WITHDRAWAL.type -> MaterialTheme.colorScheme.error
        OperationType.TRANSFER.type -> MaterialTheme.colorScheme.primary
        OperationType.DELETE.type -> MaterialTheme.colorScheme.error
        else -> Color.Gray
    }

    val amountColor = when (operation.type) {
        OperationType.DEPOSIT.type -> Color(0xFF059300)
        OperationType.WITHDRAWAL.type -> MaterialTheme.colorScheme.error
        OperationType.TRANSFER.type -> Color.Gray
        OperationType.DELETE.type -> MaterialTheme.colorScheme.error
        else -> Color.Gray
    }

    val signed = operation.type != OperationType.TRANSFER.type

    val title = when (operation.type) {
        OperationType.DEPOSIT.type -> stringResource(R.string.to, operation.title)
        OperationType.WITHDRAWAL.type -> stringResource(R.string.from, operation.title)
        OperationType.TRANSFER.type -> operation.title
        OperationType.DELETE.type -> stringResource(R.string.deleted, operation.title)
        else -> ""
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(43.dp)
                .clip(CircleShape),
            color = iconColor
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = operationType,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = title,
                modifier = Modifier
                    .widthIn(240.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = formatMoneySigned(operation.amount, operation.currency, signed = signed),
            style = MaterialTheme.typography.bodyLarge,
            color = amountColor
        )
    }

    operation.comment?.let {
        when (operation.type) {
            OperationType.DELETE.type -> CommentSection(comment = stringResource(R.string.goal_deleted))

            OperationType.TRANSFER.type -> CommentSection(comment = it)

            OperationType.WITHDRAWAL.type -> CommentSection(comment = it)

            OperationType.DEPOSIT.type -> CommentSection(comment = it)
        }
    }
}

