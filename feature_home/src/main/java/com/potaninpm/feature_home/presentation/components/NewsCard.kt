package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.domain.model.NewsArticle

@Composable
fun NewsCard(
    article: NewsArticle,
    onArticleClick: () -> Unit,
    onImageClicked: (String) -> Unit
) {

    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onArticleClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            NewsImage(
                article,
                onImageClicked = { imageUrl ->
                    onImageClicked(imageUrl)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = article.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.abstract,
                maxLines = 3,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateAndSource(
                    date = article.publishedAt,
                    source = article.source
                )

                Button(
                    modifier = Modifier,
                    onClick = {
                        onArticleClick()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.open),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun NewsImage(
    article: NewsArticle,
    onImageClicked: (String) -> Unit = {}
) {
    if (article.imageUrl.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.error_24px),
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .alpha(0.8f)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Text(
                stringResource(R.string.image_not_found),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    } else {
        Image(
            painter = rememberAsyncImagePainter(article.imageUrl),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onImageClicked(article.imageUrl)
                }
        )
    }
}

@Composable
fun DateAndSource(
    date: String,
    source: String
) {
    Column(
        modifier = Modifier
    ) {
        Text(text = source, style = MaterialTheme.typography.labelSmall, fontSize = 13.sp, fontStyle = FontStyle.Italic)

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = date, style = MaterialTheme.typography.labelSmall, fontSize = 14.sp)
    }
}
