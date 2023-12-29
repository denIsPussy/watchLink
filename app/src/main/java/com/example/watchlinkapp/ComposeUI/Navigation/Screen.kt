package com.example.watchlinkapp.ComposeUI.Navigation

import androidx.annotation.StringRes
import com.example.watchlinkapp.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: Int? = null,
    val showInBottomBar: Boolean = true,
    val isAuthenticated: Boolean = true,
) {
    MovieCatalog("movie-catalog", R.string.movie_catalog_title, R.drawable.home),
    Profile("profile", R.string.profile_title, R.drawable.account_circle),
    Search("search", R.string.search_title, R.drawable.magnify),
    Filter("filter", R.string.filter_title, R.drawable.baseline_filter_alt_24),
    Report("report", R.string.report_title, R.drawable.file_document_outline),
    Signup(
        "signup",
        R.string.signup_title,
        R.drawable.icon_profile,
        showInBottomBar = false,
        isAuthenticated = false
    ),
    Login(
        "login",
        R.string.login_title,
        R.drawable.icon_profile,
        showInBottomBar = false,
        isAuthenticated = false
    ),
    MovieView(
        "movie-view/{id}",
        R.string.movie_view_title,
        R.drawable.icon_profile,
        showInBottomBar = false
    );

    companion object {
        val bottomBarItems = listOf(
            MovieCatalog,
            Profile,
            Search,
            Filter,
            Report
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}