package com.example.movieapp.data.utils


// screens we will be using
sealed class Screen(val route: String,
    val name : String) {


    object Home: Screen("main", "Main Screen")
    object Details: Screen("details", "Details")
    object Favorites : Screen("favorites", "Favorites")
}