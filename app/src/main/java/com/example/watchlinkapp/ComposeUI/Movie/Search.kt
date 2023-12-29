package com.example.watchlinkapp.ComposeUI.Movie


import android.graphics.BitmapFactory
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.watchlinkapp.ComposeUI.AppViewModelProvider
import com.example.watchlinkapp.ComposeUI.Navigation.Screen
import com.example.watchlinkapp.Entities.Model.Movie.Movie
import com.example.watchlinkapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Search(
    navController: NavController,
    viewModel: MovieCatalogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val moviesListUiState = viewModel.movieListUiState.collectAsLazyPagingItems()
    var searchText by remember { mutableStateOf("") }
    val filteredMovies = remember { mutableStateListOf<Movie>() }

    LaunchedEffect(Unit, searchText) {
        filteredMovies.clear()
        if (searchText.isEmpty()) {
            for (i in 0 until moviesListUiState.itemCount) {
                val movie = moviesListUiState[i]
                if (movie != null) {
                    filteredMovies.add(movie)
                }
            }
        } else {
            for (i in 0 until moviesListUiState.itemCount) {
                val movie = moviesListUiState[i]
                if (movie != null && movie.title.startsWith(searchText, ignoreCase = true)) {
                    filteredMovies.add(movie)
                }
            }
        }
    }
    val refreshScope = rememberCoroutineScope()
    var refreshingMovie by remember { mutableStateOf(false) }
    fun refreshMovie() = refreshScope.launch {
        refreshingMovie = true
        moviesListUiState.refresh()
        refreshingMovie = false
    }

    val stateMovie = rememberPullRefreshState(refreshingMovie, ::refreshMovie)

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            searchDisplay = searchText,
            onSearchDisplayChanged = { text -> searchText = text },
            onSearchDisplayClosed = { searchText = "" },
            modifier = Modifier.fillMaxWidth(),
            tint = Color.White
        )
        Text(
            text = "Возможно тебя заинтересует",
            color = colorResource(id = R.color.textColor),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .pullRefresh(stateMovie)
                .background(colorResource(id = R.color.backgroundColor))
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            if (searchText.isEmpty()) {
                items(
                    count = moviesListUiState.itemCount,
                    key = moviesListUiState.itemKey(),
                    contentType = moviesListUiState.itemContentType()
                ) { index ->
                    val movie = moviesListUiState[index]
                    MovieItem(movie = movie!!, navController = navController)
                }
            } else {
                items(filteredMovies) { movie ->
                    MovieItem(movie = movie, navController = navController)
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearchDisplayClosed: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val focusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    DisposableEffect(key1 = onBackPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                focusManager.clearFocus()
                onSearchDisplayClosed()
            }
        }
        onBackPressedDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(searchDisplay, TextRange(searchDisplay.length)))
    }

    TextField(
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
            focusedContainerColor = colorResource(id = R.color.backgroundNavBarColor),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onSearchDisplayChanged(it.text)
        },
        trailingIcon = {
        },
        modifier = modifier,
        label = {
            Text(text = "Поиск...", color = tint)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSearchDisplayClosed()
            }
        ),
    )
}

@Composable
fun MovieItem(
    movie: Movie,
    navController: NavController
) {
    val movieId = Screen.MovieView.route.replace("{id}", movie.movieId.toString())
    Card(
        modifier = Modifier
            .width(110.dp)
            .fillMaxWidth()
            .clickable { navController.navigate(movieId) },
        shape = RoundedCornerShape(5.dp)
    ) {
        val decodedBitmap = BitmapFactory.decodeByteArray(movie.image, 0, movie.image!!.size)
        val imageBitmap = decodedBitmap.asImageBitmap()
        Image(
            bitmap = imageBitmap,
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}