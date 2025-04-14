package com.potaninpm.feature_finances.presentation.components.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.potaninpm.feature_finances.R

@Composable
fun GoalActionMenu(
    onDelete: () -> Unit,
    onWithdraw: () -> Unit,
    onTransfer: () -> Unit,
    onDeposit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
         Icon(
             painter = painterResource(id = R.drawable.more_vert_24px),
             contentDescription = stringResource(R.string.extra_actions),
             modifier = Modifier.clickable { expanded = true }
         )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onTransfer()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.transfer_money),
                    )
                },
                text = {
                    Text(stringResource(R.string.transfer))
                }
            )
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDeposit()
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.add_24px),
                        contentDescription = stringResource(R.string.add),
                    )
                },
                text = {
                    Text(stringResource(R.string.deposit))
                }
            )

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onWithdraw()
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.remove_24px),
                        contentDescription = stringResource(R.string.withdraw),
                    )
                },
                text = {
                    Text(stringResource(R.string.withdraw))
                }
            )

            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDelete()
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_24px),
                        contentDescription = stringResource(R.string.delete),
                        tint = Color.Red
                    )
                },
                text = {
                    Text(stringResource(R.string.delete))
                }
            )
        }
    }
}