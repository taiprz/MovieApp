package com.example.movieapp.ui.home

import com.example.movieapp.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val popularMovieListPage: Int = 1,
    val favoriteMovieListPage: Int = 1,
    val isCurrentPopularScreen : Boolean = true,
    val popularMovieList : List<Movie> = emptyList(),
    val favoriteMovieList : List<Movie> = emptyList()
)