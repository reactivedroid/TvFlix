package com.android.tvflix.main

import android.widget.Toast
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.android.tvflix.R
import com.android.tvflix.config.FavoritesFeatureFlag
import com.android.tvflix.db.favouriteshow.FavoriteShow
import com.android.tvflix.favorite.FavoriteShowState
import com.android.tvflix.favorite.FavoriteShowsViewModel
import com.android.tvflix.home.HomeViewData
import com.android.tvflix.home.HomeViewModel
import com.android.tvflix.home.HomeViewState
import com.android.tvflix.network.home.Show
import com.android.tvflix.shows.ShowsViewModel
import com.android.tvflix.splash.SplashViewModel
import com.android.tvflix.splash.SplashViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @JvmField
    @FavoritesFeatureFlag
    @Inject
    var favoritesFeatureEnable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvFlixApp(favoritesFeatureEnable = favoritesFeatureEnable)
        }
    }
}

private object Route {
    const val Splash = "splash"
    const val Home = "home"
    const val Shows = "shows"
    const val Favorites = "favorites"
}

@Composable
private fun TvFlixApp(favoritesFeatureEnable: Boolean) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Route.Splash

    Scaffold(
        topBar = {
            AppBar(
                route = currentRoute,
                favoritesFeatureEnable = favoritesFeatureEnable,
                onShowsClick = { navController.navigate(Route.Shows) { launchSingleTop = true } },
                onFavoritesClick = { navController.navigate(Route.Favorites) { launchSingleTop = true } },
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash,
            modifier = if (currentRoute == Route.Splash) Modifier else Modifier.padding(padding)
        ) {
            composable(Route.Splash) {
                SplashRoute(navController = navController)
            }
            composable(Route.Home) {
                HomeRoute(favoritesFeatureEnable = favoritesFeatureEnable)
            }
            composable(Route.Shows) {
                ShowsRoute()
            }
            composable(Route.Favorites) {
                FavoritesRoute()
            }
        }
    }
}

@Composable
private fun AppBar(
    route: String,
    favoritesFeatureEnable: Boolean,
    onShowsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBackClick: () -> Unit
) {
    if (route == Route.Splash) return

    TopAppBar(
        title = {
            Text(
                text = when (route) {
                    Route.Shows -> stringResource(id = R.string.shows)
                    Route.Favorites -> stringResource(id = R.string.favorite_shows)
                    else -> stringResource(id = R.string.app_name)
                }
            )
        },
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = colorResource(id = R.color.white),
        navigationIcon = if (route == Route.Shows || route == Route.Favorites) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            null
        },
        actions = {
            if (route == Route.Home) {
                Text(
                    text = stringResource(id = R.string.shows),
                    modifier = Modifier
                        .testTag("nav_shows")
                        .clickable(onClick = onShowsClick)
                        .padding(8.dp)
                )
                if (favoritesFeatureEnable) {
                    IconButton(
                        onClick = onFavoritesClick,
                        modifier = Modifier.testTag("nav_favorites")
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.favorite),
                            contentDescription = stringResource(id = R.string.favorite_shows),
                            tint = colorResource(id = R.color.colorAccent)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun SplashRoute(navController: NavHostController) {
    val splashViewModel: SplashViewModel = hiltViewModel()
    val splashState by splashViewModel.splashViewStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        splashViewModel.fetchConfig()
    }

    LaunchedEffect(splashState) {
        if (splashState is SplashViewState.NavigateToHome) {
            navController.navigate(Route.Home) {
                popUpTo(Route.Splash) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen")
            .background(colorResource(id = R.color.colorPrimaryDark)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_splash),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = colorResource(id = R.color.colorAccent))
        }
    }
}

@Composable
private fun HomeRoute(favoritesFeatureEnable: Boolean) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeState by homeViewModel.homeViewStateFlow.collectAsState()
    val context = LocalContext.current
    var homeViewData by remember { mutableStateOf<HomeViewData?>(null) }

    LaunchedEffect(Unit) {
        homeViewModel.onScreenCreated()
    }

    LaunchedEffect(homeState) {
        when (val state = homeState) {
            is HomeViewState.Success -> homeViewData = state.homeViewData
            is HomeViewState.NetworkError ->
                state.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            is HomeViewState.AddedToFavorites -> Toast
                .makeText(
                    context,
                    context.getString(R.string.added_to_favorites, state.show.name),
                    Toast.LENGTH_SHORT
                )
                .show()
            is HomeViewState.RemovedFromFavorites -> Toast
                .makeText(
                    context,
                    context.getString(R.string.removed_from_favorites, state.show.name),
                    Toast.LENGTH_SHORT
                )
                .show()
            HomeViewState.Loading -> Unit
        }
    }

    when {
        homeViewData != null -> {
            HomeScreen(
                homeViewData = homeViewData!!,
                favoritesFeatureEnable = favoritesFeatureEnable,
                onFavoriteClick = { episode ->
                    val updatedList = homeViewData!!.episodes.map {
                        if (it.id == episode.id) {
                            it.copy(
                                showViewData = it.showViewData.copy(
                                    isFavoriteShow = !it.showViewData.isFavoriteShow
                                )
                            )
                        } else {
                            it
                        }
                    }
                    homeViewData = homeViewData!!.copy(episodes = updatedList)
                    homeViewModel.onFavoriteClick(episode.showViewData)
                }
            )
        }
        homeState is HomeViewState.Loading -> CenterLoader()
        else -> CenterError(text = "Unable to load shows.")
    }
}

@Composable
private fun HomeScreen(
    homeViewData: HomeViewData,
    favoritesFeatureEnable: Boolean,
    onFavoriteClick: (HomeViewData.EpisodeViewData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = homeViewData.heading,
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.white),
            modifier = Modifier
                .testTag("home_heading")
                .padding(vertical = 8.dp)
        )
        val rows = remember(homeViewData.episodes) { homeViewData.episodes.chunked(2) }
        LazyColumn(
            modifier = Modifier.testTag("home_list"),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(rows.size) { rowIndex ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    rows[rowIndex].forEach { episode ->
                        ShowPosterCard(
                            imageUrl = episode.showViewData.show.image?.get("original"),
                            favoriteEnabled = favoritesFeatureEnable,
                            isFavorite = episode.showViewData.isFavoriteShow,
                            onFavoriteClick = { onFavoriteClick(episode) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rows[rowIndex].size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowPosterCard(
    imageUrl: String?,
    favoriteEnabled: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.show_image),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (favoriteEnabled) {
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.favorite else R.drawable.favorite_border
                    ),
                    contentDescription = "Favorite",
                    tint = colorResource(id = R.color.colorAccent)
                )
            }
        }
    }
}

@Composable
private fun ShowsRoute() {
    val showsViewModel: ShowsViewModel = hiltViewModel()
    val shows = remember(showsViewModel) { showsViewModel.shows() }.collectAsLazyPagingItems()

    when (val refresh = shows.loadState.refresh) {
        is LoadState.Loading -> if (shows.itemCount == 0) CenterLoader()
        is LoadState.Error -> if (shows.itemCount == 0) {
            CenterError(
                text = refresh.error.localizedMessage ?: "No internet connection",
                showRetry = true,
                onRetry = shows::retry
            )
        }
        is LoadState.NotLoading -> Unit
    }

    if (shows.itemCount > 0) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("shows_list"),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shows.itemCount) { index ->
                shows[index]?.let { ShowDetailsCard(show = it) }
            }
            if (shows.loadState.append is LoadState.Loading) {
                item { CenterLoader(modifier = Modifier.fillMaxWidth()) }
            }
            if (shows.loadState.append is LoadState.Error) {
                item {
                    CenterError(
                        text = (shows.loadState.append as LoadState.Error).error.localizedMessage
                            ?: "Failed to load more",
                        showRetry = true,
                        onRetry = shows::retry
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowDetailsCard(show: Show) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = show.image?.get("original"),
                contentDescription = stringResource(id = R.string.show_image),
                modifier = Modifier
                    .width(120.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = show.name, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                show.summary?.let {
                    Text(
                        text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                        maxLines = 4,
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (show.rating?.get("average") != null) {
                        stringResource(id = R.string.rating, show.rating?.get("average") ?: "")
                    } else {
                        stringResource(id = R.string.not_rated)
                    },
                    style = MaterialTheme.typography.caption
                )
                show.premiered?.let {
                    Text(
                        text = stringResource(id = R.string.premiered_on, it),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesRoute() {
    val favoriteShowsViewModel: FavoriteShowsViewModel = hiltViewModel()
    val favoriteState by favoriteShowsViewModel.favoriteShowsStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        favoriteShowsViewModel.loadFavoriteShows()
    }

    when (val state = favoriteState) {
        FavoriteShowState.Loading -> CenterLoader()
        is FavoriteShowState.AllFavorites -> FavoriteShowsGrid(favoriteShows = state.favoriteShows)
        FavoriteShowState.Empty -> CenterError(text = stringResource(id = R.string.favorite_hint_msg))
        is FavoriteShowState.Error -> CenterError(text = state.message)
        is FavoriteShowState.AddedToFavorites -> Unit
        is FavoriteShowState.RemovedFromFavorites -> Unit
    }
}

@Composable
private fun FavoriteShowsGrid(favoriteShows: List<FavoriteShow>) {
    val rows = remember(favoriteShows) { favoriteShows.chunked(2) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("favorites_list")
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(rows.size) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                rows[rowIndex].forEach { show ->
                    ShowPosterCard(
                        imageUrl = show.imageUrl,
                        favoriteEnabled = false,
                        isFavorite = false,
                        onFavoriteClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rows[rowIndex].size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CenterLoader(modifier: Modifier = Modifier.fillMaxSize()) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colorResource(id = R.color.colorAccent))
    }
}

@Composable
private fun CenterError(
    text: String,
    showRetry: Boolean = false,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = text, color = colorResource(id = R.color.red))
            if (showRetry && onRetry != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRetry,
                    modifier = Modifier.testTag("retry")
                ) {
                    Text(text = stringResource(id = R.string.retry))
                }
            }
        }
    }
}
