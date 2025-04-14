package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.potaninpm.core.ui.components.CustomElevatedCard

@Composable
fun GoalCard(
    title: String,
    dateOfReaching: String,
    currentAmount: Long,
    targetAmount: Long,
    currency: String,
    onDeleteClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onTransferClick: () -> Unit,
    onDepositClick: () -> Unit
) {
    CustomElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        background = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            GoalTextInfo(
                title = title,
                dateOfReaching = dateOfReaching,
                currentAmount = currentAmount,
                targetAmount = targetAmount,
                currency = currency
            )

            GoalStatAndAction(
                currentAmount = currentAmount,
                targetAmount = targetAmount,
                onDeleteClick = onDeleteClick,
                onWithdrawClick = onWithdrawClick,
                onTransferClick = onTransferClick,
                onDepositClick = onDepositClick
            )
        }
    }
}