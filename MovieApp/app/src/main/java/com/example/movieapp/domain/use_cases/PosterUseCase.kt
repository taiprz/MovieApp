package com.example.movieapp.domain.use_cases

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.services.MovieAPI
import javax.inject.Inject

class PosterUseCase @Inject constructor() {

    fun loadPoster(movie : Movie): String {

        return MovieAPI.BASE_IMAGE_URL + movie.posterPath
    }
}