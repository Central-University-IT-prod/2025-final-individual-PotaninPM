package com.potaninpm.core.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.potaninpm.core.R
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleWebView(
    selectedUrl: String,
    title: String,
    webImageUrl: String,
    rootNavController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.article)) },
                actions = {
                    Button(
                        onClick = {
                            val encodedUrl = URLEncoder.encode(selectedUrl, "UTF-8")
                            val encodedImageUrl = URLEncoder.encode(webImageUrl, "UTF-8")

                            rootNavController.navigate("create_post/$encodedUrl/$title/$encodedImageUrl")
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.create))
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            rootNavController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier.padding(innerPadding),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(selectedUrl)
                }
            }
        )
    }
}