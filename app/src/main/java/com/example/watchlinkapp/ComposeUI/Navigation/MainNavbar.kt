package com.example.watchlinkapp.ComposeUI.Navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.watchlinkapp.ComposeUI.Movie.MovieCatalog
import com.example.watchlinkapp.ComposeUI.Movie.MovieView
import com.example.watchlinkapp.ComposeUI.Movie.MoviesByDate
import com.example.watchlinkapp.ComposeUI.Movie.Report
import com.example.watchlinkapp.ComposeUI.Movie.Search
import com.example.watchlinkapp.ComposeUI.User.Login
import com.example.watchlinkapp.ComposeUI.User.Profile
import com.example.watchlinkapp.ComposeUI.User.Signup
import com.example.watchlinkapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(
    navController: NavHostController,
    currentScreen: Screen?
) {
    var showImage by remember { mutableStateOf(true) }
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor =  colorResource(id = R.color.backgroundColor),
            titleContentColor = colorResource(id = R.color.statusBarTextColor),
        ),
        title = {
            if (showImage) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Image(
                            painter = painterResource(id = R.drawable.ivi),
                            contentDescription = "ivi",
                            Modifier
                                .padding(end = 14.dp)
                                .size(50.dp)
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (
                navController.previousBackStackEntry != null && (currentScreen == null || !currentScreen.showInBottomBar)
            ) {
                showImage = false
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                showImage = true
            }
        }
    )
}

@Composable
fun Navbar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.backgroundNavBarColor),
                RoundedCornerShape(10.dp)
            ), horizontalArrangement = Arrangement.SpaceAround
    ) {
        Screen.bottomBarItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val background =
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
            val contentColor =
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = screen.icon!!),
                        contentDescription = null,
                        tint = contentColor
                    )
                    AnimatedVisibility(
                        visible = isSelected
                    ) {
                        Text(
                            text = stringResource(screen.resourceId),
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Navhost(
    navController: NavHostController,
    innerPadding: PaddingValues, modifier:
    Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Screen.Login.route,
        modifier.padding(innerPadding)
    ) {
        composable(Screen.MovieCatalog.route) { MovieCatalog(navController) }
        composable(Screen.Profile.route) { Profile() }
        composable(Screen.Signup.route) { Signup(navController) }
        composable(Screen.Login.route) { Login(navController) }
        composable(Screen.Search.route) { Search(navController) }
        composable(Screen.Filter.route) { MoviesByDate(navController) }
        composable(Screen.Report.route) { Report(navController) }
        composable(
            Screen.MovieView.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { MovieView(navController) }
        }
    }
}

@Composable
fun MainNavbar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = currentDestination?.route?.let { Screen.getItem(it) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            if (currentScreen != null) {
                if (currentScreen.isAuthenticated) {
                    Topbar(navController, currentScreen)
                }
            }
        },
        bottomBar = {
            if (currentScreen == null || (currentScreen.showInBottomBar && currentScreen.isAuthenticated)) {
                Navbar(
                    navController, currentDestination,
                    Modifier
                        .padding(all = 8.dp)
                        .background(colorResource(id = R.color.backgroundColor))
                )
            }
        },
        containerColor = colorResource(id = R.color.backgroundColor)
    ) { innerPadding ->
        Navhost(navController, innerPadding)
    }
}