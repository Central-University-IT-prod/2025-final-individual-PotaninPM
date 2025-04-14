package com.potaninpm.feature_feed.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.potaninpm.core.AnalyticsManager
import com.potaninpm.feature_feed.R
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.presentation.components.CommentsBottomSheet
import com.potaninpm.feature_feed.presentation.components.PostCard
import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    rootNavController: NavController,
    postsViewModel: PostsViewModel = koinViewModel()
) {
    val allPosts by postsViewModel.allPostsFlow.collectAsState()
    val favoritePosts by postsViewModel.favoritePostsFlow.collectAsState()
    val myPosts by postsViewModel.myPostsFlow.collectAsState()

    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    var selectedPostAttached by rememberSaveable { mutableStateOf<PostEntity?>(null) }

    var selectedPost by remember { mutableStateOf<PostEntity?>(null) }

    if (selectedPost != null) {
        CommentsBottomSheet(
            post = selectedPost!!,
            onDismiss = { selectedPost = null }
        )
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                currentTab = page
            }
    }

    val allHeaders = listOf(
        stringResource(R.string.all_posts),
        stringResource(R.string.favorite),
        stringResource(R.string.my)
    )

    if (selectedPostAttached == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                        ) {
                            item {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            items(allHeaders) { header ->
                                val index = allHeaders.indexOf(header)
                                Text(
                                    text = header,
                                    modifier = Modifier
                                        .clickable {
                                            currentTab = index
                                            scope.launch { pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    style = if (currentTab == index) {
                                        MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    } else {
                                        MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                            color = Color.Gray
                                        )
                                    }
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                AnalyticsManager.logEvent(
                                    eventName = "create_post_button_clicked",
                                    properties = mapOf("create_post_button" to "clicked")
                                )

                                rootNavController.navigate("create_post/${""}/${""}/${""}")
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_circle_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(bottom = 85.dp)
            ) { page ->
                when (page) {
                    0 -> PostList(
                        posts = allPosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = {
                            postsViewModel.favoritePost(it)
                        },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedPostAttached = it
                        }
                    )

                    1 -> PostList(
                        posts = favoritePosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = {
                            postsViewModel.favoritePost(it)
                        },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedPostAttached = it
                        }
                    )

                    2 -> PostList(
                        posts = myPosts,
                        onPostClick = {

                        },
                        onLongClickFavorite = { postsViewModel.favoritePost(it) },
                        onShowComments = {
                            selectedPost = it
                        },
                        onArticleClick = {
                            selectedPostAttached = it
                        }
                    )
                }
            }
        }
    } else {
        val encodedUrl = URLEncoder.encode(selectedPostAttached!!.webUrl, "UTF-8")
        val encodedImageUrl = URLEncoder.encode(selectedPostAttached!!.webImageUrl, "UTF-8")


        rootNavController.navigate("article_web_view/$encodedUrl/${selectedPostAttached!!.webTitle}/${encodedImageUrl}")

        selectedPostAttached = null
    }
}

@Composable
fun PostList(
    posts: List<PostEntity>,
    onPostClick: (PostEntity) -> Unit,
    onArticleClick: (PostEntity) -> Unit,
    onShowComments: (PostEntity) -> Unit,
    onLongClickFavorite: (PostEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(posts, key = { post -> post.id }) { post ->
            PostCard(
                post = post,
                onPostClick = onPostClick,
                onLongPostClick = onLongClickFavorite,
                onArticleClick = onArticleClick,
                onFavorite = onLongClickFavorite,
                onShowComments = {
                    onShowComments(post)
                }
            )
        }
    }
}
