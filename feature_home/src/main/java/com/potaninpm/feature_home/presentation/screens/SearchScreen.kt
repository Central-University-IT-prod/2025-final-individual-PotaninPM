package com.potaninpm.feature_home.presentation.screens

import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.core.functions.startVoiceRecognition
import com.potaninpm.core.ui.components.CustomElevatedCard
import com.potaninpm.core.ui.components.shimmerCards.ShimmerNewsCard
import com.potaninpm.core.ui.components.shimmerCards.ShimmerSearchTicker
import com.potaninpm.core.ui.screens.FullScreenImageDialog
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.model.SearchResults
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.presentation.components.NewsCard
import com.potaninpm.feature_home.presentation.components.SearchCategoriesCard
import com.potaninpm.feature_home.presentation.components.searchBar.SearchBar
import com.potaninpm.feature_home.presentation.viewModels.SearchViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val query by searchViewModel.query.collectAsState()

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    val focusRequester = remember { FocusRequester() }

    val results by searchViewModel.searchResults.collectAsState()

    val onMicClick = {
        startVoiceRecognition(
            context,
            speechRecognizer,
            onError = {

            }
        ) { recognizedText ->
            searchViewModel.setQuery(recognizedText)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler {
        searchViewModel.clearAll()
        navController.popBackStack()
    }

    SearchScreenContent(
        query = query,
        onQueryChange = { newQuery ->
            searchViewModel.setQuery(newQuery)
        },
        results = results,
        focusRequester = focusRequester,
        onMicClick = onMicClick,
        onClear = {
            searchViewModel.clearAll()
            navController.popBackStack()
        },
        onArticleClick = { new ->
            val encodedUrl = URLEncoder.encode(new.webUrl, "UTF-8")
            val encodedImageUrl = URLEncoder.encode(new.imageUrl, "UTF-8")

            AnalyticsManager.logEvent(
                eventName = "article_in_search_click",
                properties = mapOf("article_in_search_click" to "clicked")
            )

            navController.navigate("article_web_view/$encodedUrl/${new.title}/${encodedImageUrl}")
        },
        onLoadMore = {
            AnalyticsManager.logEvent(
                eventName = "more_articles_in_search_click",
                properties = mapOf("more_articles_in_search" to "clicked")
            )
            searchViewModel.loadMore()
        }
    )

}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onMicClick: () -> Unit,
    results: SearchResults,
    focusRequester: FocusRequester,
    onArticleClick: (NewsArticle) -> Unit,
    onLoadMore: () -> Unit
) {
    val categories by remember {
        mutableStateOf(
            listOf(
                R.string.tickers,
                R.string.news
            )
        )
    }

    var selectedCategory by remember { mutableIntStateOf(categories.first()) }

    val state = rememberScrollState()

    Scaffold(
        topBar = {

        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                focusRequester = focusRequester,
                onMicClick = {
                    onMicClick()
                },
                onClear = {
                    onClear()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.width(20.dp))
                }

                items(categories) { name ->
                    SearchCategoriesCard(
                        category = stringResource(name),
                        selected = selectedCategory == name,
                        onClick = {
                            selectedCategory = name
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (query.isNotEmpty()) {
                    when (selectedCategory) {
                        R.string.tickers -> {
                            TickersListSearch(
                                tickers = results.tickers
                            )
                        }
                        R.string.news -> {
                            NewsListSearch(
                                news = results.news,
                                onArticleClick = { new ->
                                    onArticleClick(new)
                                },
                                onLoadMore = {
                                    onLoadMore()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsListSearch(
    news: List<NewsArticle>,
    onArticleClick: (NewsArticle) -> Unit,
    onLoadMore: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1000)
            isLoading = false
        }
    }

    var openImage by remember { mutableStateOf<String?>(null) }

    if (openImage != null) {
        FullScreenImageDialog(
            imageUrl = openImage!!,
            onDismiss = {
                openImage = null
            }
        )
    }

    if (news.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.news),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier
                    .align(Alignment.Start)
            )

            news.forEach { new ->
                NewsCard(
                    article = new,
                    onArticleClick = {
                        onArticleClick(new)
                    },
                    onImageClicked = { image ->
                        openImage = image
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                onClick = {
                    onLoadMore()
                    isLoading = true
                },
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.surface
                    )
                } else {
                    Text(
                        stringResource(R.string.load_more),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

    } else {
        ShimmerNewsCard()
    }
}

@Composable
fun TickersListSearch(
    tickers: List<Ticker>
) {
    var tickerClicked by remember { mutableStateOf<Ticker?>(null) }

    if (tickerClicked != null) {
        ChatAiBottomSheet(
            companyName = tickerClicked?.companyName ?: "",
            onDismiss = {
                tickerClicked = null
            }
        )
    }

    if (tickers.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.tickers),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier
            )

            CustomElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                background = null
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                tickers.forEach { ticker ->
                    if (ticker.currentPrice != 0.0f) {
                        TickerInfoSearch(
                            ticker = ticker,
                            onTickerClick = {
                                tickerClicked = it
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    } else {
        ShimmerSearchTicker()
    }
}

@Composable
fun TickerInfoSearch(
    ticker: Ticker,
    onTickerClick: (Ticker) -> Unit
) {
    val priceColor = if (ticker.change >= 0) Color(0xFF05B000) else Color.Red

    val painter = if (ticker.logoUrl.isNullOrEmpty()) {
        painterResource(id = R.drawable.error_24px)
    } else {
        rememberAsyncImagePainter(ticker.logoUrl)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .clickable {
                onTickerClick(ticker)
            },
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
                    modifier = Modifier
                        .widthIn(max = 170.dp),
                    maxLines = 1,
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

