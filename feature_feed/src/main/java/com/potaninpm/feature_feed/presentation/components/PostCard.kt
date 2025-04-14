package com.potaninpm.feature_feed.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.core.ui.screens.FullScreenImageDialog
import com.potaninpm.core.ui.components.UserAvatar
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.domain.model.TagTypes
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: PostEntity,
    onPostClick: (PostEntity) -> Unit,
    onArticleClick: (PostEntity) -> Unit,
    onLongPostClick: (PostEntity) -> Unit,
    onFavorite: (PostEntity) -> Unit,
    onShowComments: (PostEntity) -> Unit
) {

    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSystemInDarkTheme()) Color(22, 30, 37, 255).copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .combinedClickable(
                onClick = {
                    onPostClick(post)
                },
                onLongClick = {
                    onLongPostClick(post)
                },
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
        ) {
            PostHeader(post, onFavorite)

            Spacer(modifier = Modifier.height(12.dp))
            PostImagesSection(post)

            Spacer(modifier = Modifier.height(12.dp))
            PostTagsSection(post)

            if (post.webUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AttachedArticleSection(
                    onArticleClick = {
                        onArticleClick(post)
                    },
                    webTitle = post.webTitle,
                    webImageUrl = post.webImageUrl
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            PostTextSection(post, onPostClick)

            CommentsSection(post, onShowComments)
        }
    }
}

@Composable
fun PostHeader(
    post: PostEntity,
    onFavorite: (PostEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        UserAvatar(post.author)

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = post.author,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (post.likes > 0) {
                Text(
                    text = post.likes.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = if (post.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.toggle_favorite),
                modifier = Modifier
                    .clickable {
                        onFavorite(post)
                    },
                tint = if (post.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}

@Composable
fun PostImagesSection(
    post: PostEntity
) {
    var openImage by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf("") }

    if (openImage) {
        if (imagePath.isNotEmpty()) {
            FullScreenImageDialog(
                imagePath = imagePath,
                onDismiss = {
                    openImage = false
                    imagePath = ""
                }
            )
        }
    }

    if (post.imagePaths.isNotEmpty()) {
        when (post.imagePaths.size) {
            1 -> {
                Image(
                    painter = rememberAsyncImagePainter(
                        File(post.imagePaths.first())
                    ),
                    contentDescription = stringResource(R.string.post_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .clickable {
                            openImage = true
                            imagePath = post.imagePaths.first()
                        },
                    contentScale = ContentScale.Crop
                )
            }

            2 -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    post.imagePaths.forEach { image ->
                        Image(
                            painter = rememberAsyncImagePainter(model = File(image)),
                            contentDescription = stringResource(R.string.post_image),
                            modifier = Modifier
                                .weight(1f)
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                                .clickable {
                                    openImage = true
                                    imagePath = image
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            3 -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = File(post.imagePaths.first())),
                        contentDescription = stringResource(R.string.post_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                            .clickable {
                                openImage = true
                                imagePath = post.imagePaths.first()
                            },
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.imagePaths.drop(1).forEach { image ->
                            Image(
                                painter = rememberAsyncImagePainter(model = File(image)),
                                contentDescription = stringResource(R.string.post_image),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                                    .clickable {
                                        openImage = true
                                        imagePath = image
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            4 -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.imagePaths.take(2).forEach { image ->
                            Image(
                                painter = rememberAsyncImagePainter(model = File(image)),
                                contentDescription = stringResource(R.string.post_image),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                                    .clickable {
                                        openImage = true
                                        imagePath = image
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.imagePaths.drop(2).forEach { image ->
                            Image(
                                painter = rememberAsyncImagePainter(model = File(image)),
                                contentDescription = stringResource(R.string.post_image),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                                    .clickable {
                                        openImage = true
                                        imagePath = image
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(post.imagePaths) { image ->
                        Image(
                            painter = rememberAsyncImagePainter(model = File(image)),
                            contentDescription = stringResource(R.string.post_image),
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                                .clickable {
                                    openImage = true
                                    imagePath = image
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostTagsSection(
    post: PostEntity
) {
    val allTags = listOf(
        TagTypes(stringResource(id = R.string.business), "business"),
        TagTypes(stringResource(id = R.string.finances), "finances"),
        TagTypes(stringResource(id = R.string.entertainment), "entertainment"),
        TagTypes(stringResource(id = R.string.investments), "investments"),
        TagTypes(stringResource(id = R.string.news), "news")
    )

    val tagMap = allTags.associate { it.tagType to it.tagName }

    if (post.tags.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(post.tags) { tag ->
                TagChip(
                    tagText = {
                        Text(
                            text = stringResource(R.string.tag, tagMap[tag]!!),
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            fontWeight = FontWeight.W500,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TagChip(
    tagText: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                enabled = false,
                onClick = {

                }
            )
    ) {
        tagText()
    }
}

@Composable
fun AttachedArticleSection(
    onArticleClick: () -> Unit,
    webTitle: String,
    webImageUrl: String
) {
    Card(
        modifier = Modifier
            .height(45.dp)
            .clickable {
                onArticleClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = webImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = webTitle,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(230.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.arrow_right_24px),
                contentDescription = stringResource(R.string.show_article),
                modifier = Modifier
                    .size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PostTextSection(
    post: PostEntity,
    onPostClick: (PostEntity) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy '|' HH:mm").withZone(ZoneId.systemDefault())
    val date = formatter.format(Instant.ofEpochMilli(post.date))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ExpandableText(
            text = post.text,
            minimizedMaxLines = 3,
            readMoreText = stringResource(R.string.more),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        ) {
            onPostClick(post)
        }

        Text(
            text = date,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier
                .clickable {
                    onPostClick(post)
                }
                .align(Alignment.End)
        )
    }
}

@Composable
fun ExpandableText(
    text: String,
    minimizedMaxLines: Int = 3,
    readMoreText: String = stringResource(R.string.more),
    readLessText: String = stringResource(R.string.hide),
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var isExpanded by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(AnnotatedString(text)) }

    val color = MaterialTheme.colorScheme.primary

    Text(
        text = finalText,
        maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
        overflow = TextOverflow.Ellipsis,
        style = style,
        modifier = Modifier.clickable {
            isExpanded = !isExpanded
            onClick()
        },
        onTextLayout = { layoutResult ->
            if (!isExpanded && layoutResult.hasVisualOverflow) {
                val lastLineEnd = layoutResult.getLineEnd(minimizedMaxLines - 1, visibleEnd = true)
                val adjustedIndex = (lastLineEnd - readMoreText.length - 1).coerceAtLeast(0)

                finalText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(text.take(adjustedIndex))
                        append(context.getString(R.string.three_dots))
                    }
                    withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                        append(readMoreText)
                    }
                }
            } else if (isExpanded) {
                finalText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(text)
                        append(" ")
                    }
                    withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold)) {
                        append(readLessText)
                    }
                }
            }
        }
    )
}



@Composable
fun CommentsSection(
    post: PostEntity,
    onShowComments: (PostEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowComments(post) }
                .padding(vertical = 11.dp)
                .padding(start = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.forum_24px),
                    contentDescription = stringResource(R.string.comments),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.comments),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    fontWeight = FontWeight.Medium,
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.arrow_right_24px),
                contentDescription = stringResource(R.string.show_comments),
                modifier = Modifier
                    .size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
