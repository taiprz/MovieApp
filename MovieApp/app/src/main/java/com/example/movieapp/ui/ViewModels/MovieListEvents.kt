package com.example.movieapp.ui.ViewModels

sealed interface MovieListEvents {

    data class Paginate(val category : String) : MovieListEvents
    data class Search(val query: String) : MovieListEvents
    object Navigate : MovieListEvents
}