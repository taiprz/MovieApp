package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieById(id: Int): Flow<Resource<Movie>>

    suspend fun addFavorite(movie: Movie)

    suspend fun removeFavorite(movieId: Int)

     fun isFavorite(movieId: Int): Flow<Boolean>
}