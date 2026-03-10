package com.example.movieapp.ui.home

sealed interface MovieListEvents {
    data class Search(val query: String) : MovieListEvents
}