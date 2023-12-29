package com.example.watchlinkapp.ComposeUI.Movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.watchlinkapp.API.Model.ReleaseYearRemote
import com.example.watchlinkapp.ComposeUI.APIStatus
import com.example.watchlinkapp.ComposeUI.AppViewModelProvider
import com.example.watchlinkapp.ComposeUI.Network.ErrorPlaceholderWithoutButton
import com.example.watchlinkapp.ComposeUI.Network.LoadingPlaceholder
import com.example.watchlinkapp.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MoviesByDate(
    navController: NavController,
    viewModel: MovieCatalogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val moviesListUiState = viewModel.movieByDateListUiState.collectAsState(initial = listOf())
    val releaseYearListUiState =
        viewModel.releaseYearListUiState.collectAsState(initial = ReleaseYearRemote())
    val releaseYearList = releaseYearListUiState.value.releaseYears
    var expandedStartDate by remember { mutableStateOf(false) }
    var selectedIndexStartDate by remember { mutableStateOf(0) }
    var expandedEndDate by remember { mutableStateOf(false) }
    var selectedIndexEndDate by remember { mutableStateOf(0) }
    val buttonText = if (releaseYearList.isNotEmpty()) {
        releaseYearList[selectedIndexStartDate].toString()
    } else {
        "Год не выбран"
    }
    val buttonText2 = if (releaseYearList.isNotEmpty()) {
        releaseYearList[selectedIndexEndDate].toString()
    } else {
        "Год не выбран"
    }
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Укажите начальный год",
                    color = colorResource(id = R.color.textColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        expandedStartDate = true
                    }) {
                    Text(text = buttonText)
                }

                DropdownMenu(
                    expanded = expandedStartDate,
                    onDismissRequest = { expandedStartDate = false }
                ) {
                    releaseYearList.forEachIndexed { index, title ->
                        DropdownMenuItem(onClick = {
                            selectedIndexStartDate = index
                            expandedStartDate = false
                        }) {
                            Text(text = title.toString())
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Укажите конечный год",
                    color = colorResource(id = R.color.textColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { expandedEndDate = true }) {
                    Text(text = buttonText2)
                }

                DropdownMenu(
                    expanded = expandedEndDate,
                    onDismissRequest = { expandedEndDate = false }
                ) {
                    releaseYearList.forEachIndexed { index, title ->
                        DropdownMenuItem(onClick = {
                            selectedIndexEndDate = index
                            expandedEndDate = false
                        }) {
                            Text(text = title.toString())
                        }
                    }
                }
            }

            Button(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(5.dp), onClick = {
                viewModel.collectMoviesByDate(
                    releaseYearList[selectedIndexStartDate].toString(),
                    releaseYearList[selectedIndexEndDate].toString()
                )
            }) {
                Text(text = "Применить фильтр")
            }
        }

        when (viewModel.apiStatus) {
            APIStatus.DONE -> {
                Column {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Фильмы за период",
                            color = colorResource(id = R.color.textColor),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .background(colorResource(id = R.color.backgroundColor))
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        ) {
                            items(moviesListUiState.value) { movie ->
                                MovieItem(movie = movie, navController = navController)
                            }
                        }
                    }
                }
            }

            APIStatus.LOADING -> LoadingPlaceholder()
            else -> ErrorPlaceholderWithoutButton(
                message = viewModel.apiError
            )
        }
    }
}