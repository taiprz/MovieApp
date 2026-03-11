package com.example.movieapp.domain.repository

import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {

   suspend fun getFavorites(): Flow<List<Movie>>

    fun searchMoviesPaged(query: String,
    ): Flow<PagingData<Movie>>

    fun getAllMovies() : Flow<PagingData<Movie>>

}




