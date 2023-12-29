package com.example.watchlinkapp.ComposeUI.Movie

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.watchlinkapp.ComposeUI.APIStatus
import com.example.watchlinkapp.ComposeUI.AppViewModelProvider
import com.example.watchlinkapp.ComposeUI.Network.ErrorPlaceholder
import com.example.watchlinkapp.ComposeUI.Network.LoadingPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieView(
    navController: NavController,
    viewModel: MovieViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    if (viewModel.apiStatus == APIStatus.ERROR) {
        ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.popBackStack() }
        )
        return
    }
    when (viewModel.apiStatus) {
        APIStatus.DONE -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
            ) {
                var movie = viewModel.movieUiState.movieDetails.movie
                var genres = viewModel.movieUiState.movieDetails.genres
                movie?.let {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            val decodedBitmap =
                                BitmapFactory.decodeByteArray(movie.image, 0, movie.image!!.size)
                            val imageBitmap = decodedBitmap.asImageBitmap()
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(200.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    readOnly = true,
                                    value = movie.title,
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Title", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = genres.toCustomString(),
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Genre", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = "${movie.releaseYear}",
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Release Year", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = "${movie.duration} minutes",
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Duration", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = "${movie.rating}",
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Rating", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = movie.synopsis,
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Synopsis", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    readOnly = true,
                                    value = "${movie.director}",
                                    onValueChange = { /*TODO*/ },
                                    label = { Text("Director", color = Color.LightGray) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.LightGray,
                                        unfocusedTextColor = Color.LightGray,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }

        APIStatus.LOADING -> LoadingPlaceholder()
        else -> ErrorPlaceholder(
            message = viewModel.apiError,
            onBack = { navController.popBackStack() }
        )
    }
}