package com.example.movieapp.ui.details

import com.example.movieapp.domain.model.Movie

data class DetailState(
    val isLoading: Boolean = false,
    val movie : Movie? = null
)
