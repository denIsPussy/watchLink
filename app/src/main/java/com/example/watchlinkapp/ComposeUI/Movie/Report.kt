package com.example.watchlinkapp.ComposeUI.Movie

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.watchlinkapp.API.Model.MovieGenreCountRemote
import com.example.watchlinkapp.ComposeUI.APIStatus
import com.example.watchlinkapp.ComposeUI.AppViewModelProvider
import com.example.watchlinkapp.ComposeUI.Network.ErrorPlaceholderWithoutButton
import com.example.watchlinkapp.ComposeUI.Network.LoadingPlaceholder

@Composable
fun Report(
    navController: NavController,
    viewModel: MovieCatalogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.loadGenreMovieCount()
    }
    if (viewModel.apiStatus == APIStatus.ERROR) {
        ErrorPlaceholderWithoutButton(
            message = viewModel.apiError
        )
        return
    }
    val genreMovieCountListUiState = viewModel.genreMovieCountListUiState
    val genreMovieCountList = genreMovieCountListUiState
    when (viewModel.apiStatus) {
        APIStatus.DONE -> {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(color = Color.White, text = "Жанр", modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            color = Color.White,
                            text = "Количество фильмов",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                items(genreMovieCountList) { movieGenreCount ->
                    MovieGenreCountRow(movieGenreCount)
                }
            }
        }

        APIStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholderWithoutButton(
            message = viewModel.apiError
        )
    }
}

@Composable
fun MovieGenreCountRow(movieGenreCount: MovieGenreCountRemote) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            color = Color.White,
            text = movieGenreCount.genre!!.name,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            color = Color.White,
            text = movieGenreCount.movieCount.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}