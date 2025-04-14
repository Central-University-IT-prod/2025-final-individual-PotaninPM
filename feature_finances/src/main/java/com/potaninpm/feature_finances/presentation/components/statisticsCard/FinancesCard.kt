package com.potaninpm.feature_finances.presentation.components.statisticsCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.potaninpm.core.ui.components.CustomElevatedCard
import com.potaninpm.core.functions.formatMoneyUnsigned
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.presentation.components.goals.goalCard.GoalProgress
import kotlin.math.round

@Composable
fun FinancesCard(
    totalSavings: Long,
    totalTarget: Long,
    averageMonthlyIncome: Double,
    monthsToAchieve: Double,
    overallProgress: Float
) {
    CustomElevatedCard(
        modifier = Modifier
            .padding(top = 16.dp),
        background = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            FinancesCardItem(
                title = "${(overallProgress * 100).toInt()}%",
                subtitle = stringResource(R.string.percent_goals),
                icon = {
                    GoalProgress(
                        modifier = Modifier
                            .size(43.dp),
                        currentAmount = totalSavings,
                        targetAmount = -1,
                        overallProgress = overallProgress,
                        insideThing = {
                            Icon(
                                painter = painterResource(R.drawable.account_balance_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(totalTarget - totalSavings)} ₽",
                subtitle = stringResource(R.string.left_to_save),
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.error
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.savings_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(totalSavings)} ₽",
                subtitle = stringResource(R.string.all_saved),
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.savings_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = "${formatMoneyUnsigned(averageMonthlyIncome.toLong())} ₽",
                subtitle = stringResource(R.string.month_income),
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icome_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinancesCardItem(
                title = formatDuration(monthsToAchieve),
                subtitle = stringResource(R.string.when_end_up_saving),
                icon = {
                    Surface(
                        modifier = Modifier
                            .size(43.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.schedule_24px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                }
            )
        }
    }
}

@Composable
fun formatDuration(months: Double): String {
    return if (months == 0.0) {
        stringResource(R.string.all_goals_done)
    } else if (months == -1.0) {
        stringResource(R.string.no_idea)
    } else if (months < 1.0) {
        val days = (months * 30).toInt()

        if (days < 5) {
            stringResource(R.string.near_days)
        } else {
            stringResource(R.string.days, days)
        }
    } else if (months in 1.0..12.0) {
        val formattedMonths = round(months).toInt()
        stringResource(R.string.month_left, formattedMonths)
    } else {
        val years = (months / 12).toInt()
        val remainingMonths = (months % 12).toInt()
        stringResource(R.string.years_month_left, years, remainingMonths)
    }
}