package com.potaninpm.core.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.core.AnalyticsManager

@Composable
fun FullScreenImageDialog(
    imagePath: String = "",
    imageUri: Uri = Uri.EMPTY,
    imageBytes: ByteArray = ByteArray(0),
    imageUrl: String = "",
    onDismiss: () -> Unit
) {
    AnalyticsManager.logEvent(
        eventName = "full_image_screen_opened",
        properties = mapOf("full_image_screen" to "opened")
    )

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceAtLeast(1f)

                            offset = Offset(
                                x = (offset.x + pan.x).coerceIn(-((scale - 1) * 1920 / 2), (scale - 1) * 1920 / 2),
                                y = (offset.y + pan.y).coerceIn(-((scale - 1) * 1080 / 2), (scale - 1) * 1080 / 2)
                            )
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            ) {
                val painter = if (imageBytes.isNotEmpty()) {
                    rememberAsyncImagePainter(model = imageBytes)
                } else if (imageUri != Uri.EMPTY) {
                    rememberAsyncImagePainter(model = imageUri)
                } else if (imagePath.isNotEmpty()) {
                    rememberAsyncImagePainter(model = imagePath)
                } else {
                    rememberAsyncImagePainter(model = imageUrl)
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            onDismiss()
                        }
                        .align(Alignment.TopEnd)
                ) {
                   Icon(
                       imageVector = Icons.Default.Close,
                       contentDescription = "Close",
                       tint = MaterialTheme.colorScheme.onPrimary,
                       modifier = Modifier
                           .align(Alignment.Center)
                   )
                }
            }
        }
    }
}