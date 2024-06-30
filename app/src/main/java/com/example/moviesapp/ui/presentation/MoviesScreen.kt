package com.example.moviesapp.ui.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.moviesapp.R
import com.example.moviesapp.common.IMAGE_BASE_URL
import com.example.moviesapp.common.shimmerBrush
import com.example.moviesapp.data.remote.dto.allmovies.Movie
import com.example.moviesapp.ui.viewmodel.MovieViewModel
import com.example.nearbuy.navigation.Screen
import java.text.DecimalFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val moviePagingItems: LazyPagingItems<Movie> = viewModel.moviesList.collectAsLazyPagingItems()

    val lazyGridState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            ProjectsTopAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                onBackClick = {},
                scrollBehavior = scrollBehavior,
            )
            EmbeddedSearchBar(
                onQueryChange = { viewModel.setSearch(it) },
                modifier = Modifier
                    .padding(top = 90.dp)
                    .fillMaxWidth()
            )
        },
        content = {
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = 170.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                items(moviePagingItems.itemCount) { index ->
                    // Use local state for shimmer effect
                    var showShimmer by remember { mutableStateOf(true) }

                    // If movie is null or in loading state, show shimmer
                    val isLoading = moviePagingItems.loadState.source.refresh is LoadState.Loading ||
                            moviePagingItems.loadState.append is LoadState.Loading
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                if (moviePagingItems[index] != null) {
                                    navController.navigate("${Screen.DETAILS.route}/${moviePagingItems[index]?.id}")
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                AsyncImage(
                                    modifier = Modifier
                                        .height(250.dp)
                                        .background(
                                            shimmerBrush(
                                                targetValue = 1300f,
                                                showShimmer = showShimmer || isLoading
                                            )
                                        ),
                                    model = IMAGE_BASE_URL + moviePagingItems[index]!!.poster_path,
                                    contentDescription = "Poster",
                                    onSuccess = { showShimmer = false },
                                    contentScale = ContentScale.FillBounds
                                )
                                Box(
                                    modifier = Modifier
                                        .wrapContentWidth(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "IMDb Rating Star",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier
                                            .size(50.dp)
                                            .align(Alignment.Center)
                                    )
                                    Text(
                                        text = DecimalFormat("#.#").format(moviePagingItems[index]!!.vote_average),
                                        fontSize = 12.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
                moviePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            items(20){
                                ShimmerOnLoading()
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            items(20){
                                ShimmerOnLoading()
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            items(20){
                                ShimmerOnLoading()
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            items(20){
                                ShimmerOnLoading()
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ShimmerOnLoading(){
    Box(modifier = Modifier
        .padding(7.dp)
        .background(Color.Transparent, shape = RoundedCornerShape(8.dp))) {
        AsyncImage(
            modifier = Modifier
                .height(250.dp)
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = true
                    ),
                    RoundedCornerShape(8.dp)
                ),
            model = null,
            contentDescription = "Poster",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProjectsTopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    allowArrowBack: Boolean = false
) {
    TopAppBar(
        title = { Text("Movies") },
        modifier = modifier,
        navigationIcon = {
            if (allowArrowBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /* Handle profile */ }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit)? = null,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 10.dp)
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterStart
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onQueryChange(it)
            },
            placeholder = {
                Text("Search")
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch?.invoke(searchQuery)
                    keyboardController?.hide()
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 10.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
        )
        if (searchQuery.isNotEmpty()) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Clear Search",
                tint = Color.DarkGray,
                modifier = Modifier
                    .clickable {
                        searchQuery = ""
                        onQueryChange("")
                    }
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 10.dp)
            )
        }
    }
}
