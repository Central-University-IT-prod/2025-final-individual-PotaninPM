package com.potaninpm.feature_home.presentation.components.searchBar

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.potaninpm.feature_home.R

@Composable
fun SearchBar(
    query: String,
    onMicClick: () -> Unit,
    onClear: () -> Unit,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val context = LocalContext.current

    var microphonePermGiven by remember { mutableStateOf(false) }
    var micClicked by remember { mutableStateOf(false) }

    if (micClicked) {
        LaunchedEffect(key1 = micClicked) {
            kotlinx.coroutines.delay(5000L)
            micClicked = false
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            microphonePermGiven = isGranted
            if (!isGranted) {
                Toast.makeText(context,
                    context.getString(R.string.micro_perm_is_required), Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = {
                onQueryChange(it)
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = Color.White
            ),
            placeholder = { Text(stringResource(R.string.search)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .focusRequester(focusRequester)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.medium
                ),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    micClicked = false
                    Text(
                        stringResource(R.string.cancel),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                onQueryChange("")
                                onClear()
                            },
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    IconButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            } else {
                                microphonePermGiven = true
                                micClicked = true
                                onMicClick()
                            }
                        }
                    ) {
                        if (micClicked) {
                            Icon(painter = painterResource(id = R.drawable.mic_24px), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        } else {
                            Icon(painter = painterResource(id = R.drawable.mic_24px), contentDescription = null)
                        }
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        )
    }
}