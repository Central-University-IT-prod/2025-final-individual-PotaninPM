package com.potaninpm.feature_finances.presentation.components.goals.goalCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.potaninpm.core.functions.formatMoneyUnsigned
import com.potaninpm.feature_finances.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun GoalTextInfo(
    title: String,
    dateOfReaching: String,
    currentAmount: Long,
    targetAmount: Long,
    currency: String
) {
    val formattedCurrentAmount = formatMoneyUnsigned(currentAmount)
    val formattedTargetAmount = formatMoneyUnsigned(targetAmount)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.out_of,
                    formattedCurrentAmount,
                    currency,
                    formattedTargetAmount,
                    currency
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 7,
                modifier = Modifier
                    .width(240.dp),
                overflow = TextOverflow.Ellipsis
            )

            if (dateOfReaching != "null") {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault())
                val dateOfReachMillis = dateOfReaching.toLong()
                val currentMillis = System.currentTimeMillis()

                when {
                    dateOfReachMillis < currentMillis -> {
                        val formattedDate = formatter.format(Instant.ofEpochMilli(dateOfReachMillis))
                        Text(
                            text = stringResource(R.string.past, formattedDate),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    dateOfReachMillis < currentMillis + 24 * 60 * 60 * 1000 -> {
                        Text(
                            text = stringResource(R.string.until_tomorrow),
                            color = Color(217, 148, 0),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    else -> {
                        val formattedDate = formatter.format(Instant.ofEpochMilli(dateOfReachMillis))
                        Text(
                            text = stringResource(R.string.before, formattedDate),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.without_date),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}