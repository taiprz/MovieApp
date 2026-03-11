package com.example.movieapp.data.utils


// screens we will be using
sealed class Route(
    val route: String,
    val name : String
) {
    object Home: Route("main", "Main Screen")
    object Details: Route("details", "Details")
    object Favorites : Route("favorites", "Favorites")
}