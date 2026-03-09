package com.example.movieapp.ui.home

sealed interface MovieListEvents {

    data class Paginate(val category : String) : MovieListEvents
    data class Search(val query: String) : MovieListEvents
}