package com.potaninpm.feature_finances.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.AddButton
import com.potaninpm.feature_finances.R
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.domain.Operation
import com.potaninpm.feature_finances.presentation.components.statisticsCard.FinancesCard
import com.potaninpm.feature_finances.presentation.components.goals.dialogs.AddGoalDialog
import com.potaninpm.feature_finances.presentation.components.goals.dialogs.DepositMoneyDialog
import com.potaninpm.feature_finances.presentation.components.goals.dialogs.TransferDialog
import com.potaninpm.feature_finances.presentation.components.goals.dialogs.WithdrawDialog
import com.potaninpm.feature_finances.presentation.components.goals.goalCard.GoalCard
import com.potaninpm.feature_finances.presentation.components.operations.dialog.AddOperationDialog
import com.potaninpm.feature_finances.presentation.components.operations.section.OperationsSection
import com.potaninpm.feature_finances.presentation.viewModels.FinancesViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun groupOperationsByDate(operations: List<Operation>): Map<LocalDate, List<Operation>> {
    return operations.groupBy { operation ->
        Instant.ofEpochMilli(operation.date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

@Composable
fun FinancesScreen(
    modifier: Modifier = Modifier,
    viewModel: FinancesViewModel = koinViewModel()
) {
    modifier.fillMaxSize()
    var showAddGoalDialog by rememberSaveable { mutableStateOf(false) }
    var showAddOperationDialog by rememberSaveable { mutableStateOf(false) }

    val onAddGoalClick = {
        showAddGoalDialog = true
    }

    val onAddOperationClick = {
        showAddOperationDialog = true
    }

    if (showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onAddGoal = { title, targetAmount, currency, dueDate ->
                viewModel.addGoal(title.trim(), targetAmount, currency, dueDate)
                showAddGoalDialog = false
            }
        )
    }

    if (showAddOperationDialog) {
        val goals by viewModel.goals.collectAsState()

        AddOperationDialog(
            goals = goals,
            onDismiss = { showAddOperationDialog = false },
            onAddOperation = { goalId, amount, comment ->
                val goal = goals.find { it.id == goalId } ?: return@AddOperationDialog
                if (amount >= 0) {
                    viewModel.addDeposit(goal, amount, comment?.trim())
                } else {
                    viewModel.addWithdrawal(goal, -amount, comment?.trim())
                }
                showAddOperationDialog = false
            }
        )
    }

    FinancesScreenContent(
        viewModel = viewModel,
        onAddGoalClick = {
            onAddGoalClick()
        },
        onAddOperationClick = {
            onAddOperationClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinancesScreenContent(
    viewModel: FinancesViewModel,
    onAddGoalClick: () -> Unit,
    onAddOperationClick: () -> Unit
) {
    val state = rememberScrollState()

    val goals by viewModel.goals.collectAsState()
    val operations by viewModel.operations.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val totalTarget by viewModel.totalTarget.collectAsState()
    val averageMonthlyInflow by viewModel.averageMonthlyInflow.collectAsState()

    var fromGoal by remember { mutableStateOf<GoalEntity?>(null) }
    var actionType by remember { mutableStateOf<String?>(null) }

    val monthsToAchieve = if (averageMonthlyInflow == 0.0) -1.0 else (totalTarget - totalSavings) / averageMonthlyInflow
    val overallProgress = totalSavings.toFloat() / totalTarget.toFloat()

    if (fromGoal != null && actionType != null) {
        when (actionType) {
            "delete" -> {
                AnalyticsManager.logEvent(
                    eventName = "goal_deleted",
                    properties = mapOf("goal_deleted_click" to "clicked")
                )

                LaunchedEffect(fromGoal) {
                    viewModel.deleteGoal(fromGoal!!)
                    fromGoal = null
                    actionType = null
                }
            }
            "withdraw" -> {
                AnalyticsManager.logEvent(
                    eventName = "withdraw_btn_clicked",
                    properties = mapOf("withdraw_btn" to "clicked")
                )

                if (fromGoal!!.currentAmount == 0L) {
                    viewModel.deleteGoal(fromGoal!!)
                    fromGoal = null
                    actionType = null
                } else {
                    WithdrawDialog(
                        goal = fromGoal!!,
                        onDismiss = { fromGoal = null; actionType = null },
                        onConfirm = { amount, comment ->
                            viewModel.addWithdrawal(fromGoal!!, amount, comment?.trim())
                            fromGoal = null
                            actionType = null
                        },
                        onDeleteGoal = {
                            viewModel.deleteGoal(fromGoal!!)
                            fromGoal = null
                            actionType = null
                        }
                    )
                }
            }
            "transfer" -> {
                AnalyticsManager.logEvent(
                    eventName = "transfer_btn_clicked",
                    properties = mapOf("transfer_btn" to "clicked")
                )

                TransferDialog(
                    fromGoal = fromGoal!!,
                    availableTargetGoals = goals.filter { it.id != fromGoal!!.id },
                    onDismiss = { fromGoal = null; actionType = null },
                    onConfirm = { _, toId, amount, comment ->
                        val targetGoal = goals.find { it.id == toId }
                        if (targetGoal != null) {
                            viewModel.transferMoney(fromGoal!!, targetGoal, amount, comment?.trim())
                        }
                        fromGoal = null
                        actionType = null
                    }
                )
            }

            "deposit" -> {
                AnalyticsManager.logEvent(
                    eventName = "deposit_btn_clicked",
                    properties = mapOf("deposit_btn" to "clicked")
                )

                DepositMoneyDialog(
                    goal = fromGoal!!,
                    onDismiss = { fromGoal = null; actionType = null },
                    onConfirm = { goal, amount, comment ->
                        viewModel.addDeposit(goal, amount, comment?.trim())
                        fromGoal = null
                        actionType = null
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.finances),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                )
                HorizontalDivider()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp)
                .verticalScroll(state)
        ) {
            FinancesCard(
                totalSavings = totalSavings,
                totalTarget = totalTarget,
                averageMonthlyIncome = averageMonthlyInflow,
                monthsToAchieve = monthsToAchieve,
                overallProgress = overallProgress
            )

            YourGoalsSection(
                titleRes = R.string.goals,
                content = {
                    if (goals.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .alpha(0.7f),
                            text = stringResource(R.string.np_goals_yet),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        goals.forEach { goal ->
                            GoalCard(
                                title = goal.title,
                                dateOfReaching = goal.dueDate.toString(),
                                currentAmount = goal.currentAmount,
                                targetAmount = goal.targetAmount,
                                currency = goal.currency,
                                onDeleteClick = {
                                    fromGoal = goal
                                    actionType = "delete"
                                },
                                onWithdrawClick = {
                                    fromGoal = goal
                                    actionType = "withdraw"
                                },
                                onTransferClick = {
                                    fromGoal = goal
                                    actionType = "transfer"
                                },
                                onDepositClick = {
                                    fromGoal = goal
                                    actionType = "deposit"
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                },
                onAddGoalClick = onAddGoalClick
            )

            HorizontalDivider()

            OperationsSection(
                operations = operations,
                onAddOperationClick = onAddOperationClick,
                goalQuantity = goals.size
            )
        }
    }
}

@Composable
fun YourGoalsSection(
    @StringRes titleRes: Int,
    content: @Composable () -> Unit,
    onAddGoalClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                fontWeight = FontWeight.Bold,
            )

            AddButton(
                onAddClick = onAddGoalClick,
                title = R.string.add_goal
            )
        }
        content()
    }
}

