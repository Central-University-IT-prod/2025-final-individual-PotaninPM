package com.potaninpm.feature_feed.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.ui.components.UserAvatar
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.presentation.viewModels.CommentsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    post: PostEntity,
    onDismiss: () -> Unit,
    commentsViewModel: CommentsViewModel = koinViewModel(
        parameters = { parametersOf(post.id) }, key = "CommentsViewModel_${post.id}"
    )
) {
    AnalyticsManager.logEvent(
        eventName = "comment_bottom_sheet_opened",
        properties = mapOf("comment_bottom_sheet_opened" to "success")
    )

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val comments by commentsViewModel.comments.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {

        },
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CommentsBottomSheetHeader(
                totalComments = comments.size,
                onDismiss = onDismiss
            )

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(comments, key = { it.id }) { comment ->
                    CommentItem(comment = comment)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

            CommentInputSection { commentText ->
                if (commentText.isNotBlank()) {
                    AnalyticsManager.logEvent(
                        eventName = "user_left_comment",
                        properties = mapOf("user_left_comment" to "success")
                    )

                    commentsViewModel.addComment(commentText)
                }
            }
        }
    }
}

@Composable
fun CommentsBottomSheetHeader(totalComments: Int, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.comments_fig, totalComments),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.close))
        }
    }
}

@Composable
fun CommentInputSection(
    onAddComment: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            maxLines = 1,
            placeholder = { Text(stringResource(R.string.write_comment)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onAddComment(commentText)
                            commentText = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.send),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}

@Composable
fun CommentItem(comment: CommentEntity) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        UserAvatar(username = comment.author, size = 40)

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.author,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = timeAgo(comment.date, context),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = stringResource(R.string.like),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = comment.likes.toString(),
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(R.drawable.thumb_down_not_filled_24px),
                    contentDescription = stringResource(R.string.dislike),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = comment.dislikes.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

fun timeAgo(timestamp: Long, context: Context): String {
    val diff = System.currentTimeMillis() - timestamp
    val days = diff / (1000 * 60 * 60 * 24)

    return when {
        days >= 1 -> context.getString(R.string.days_ago, days.toString())
        else -> context.getString(R.string.today)
    }
}
