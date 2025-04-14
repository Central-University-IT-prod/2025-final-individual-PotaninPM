package com.potaninpm.feature_home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.domain.model.Ticker

@Composable
fun TickerCard(
    ticker: Ticker,
    onTickerClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val priceColor = if (ticker.change >= 0) Color(0xFF05B000) else Color.Red

    val painter = if (ticker.logoUrl.isNullOrEmpty()) {
        painterResource(id = R.drawable.error_24px)
    } else {
        rememberAsyncImagePainter(ticker.logoUrl)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable {
                onTickerClick(ticker.companyName)
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ticker.symbol,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Image(
                        painter = painterResource(if (ticker.change >= 0) R.drawable.arrow_drop_up_24px else R.drawable.arrow_drop_down_24px),
                        contentDescription = null,
                    )
                }
                ticker.companyName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = String.format(stringResource(R.string._2f), ticker.currentPrice) + " " + ticker.currency,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = if (ticker.change >= 0) "${"+%.2f".format(ticker.change)}%" else "${"%.2f".format(ticker.change)}%",
                    color = priceColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}