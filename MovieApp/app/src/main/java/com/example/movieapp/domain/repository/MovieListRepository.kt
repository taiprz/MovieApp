package com.example.movieapp.domain.repository

import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    // when paginated, needs to be true to force to get more movies
    suspend fun getMovieList(
        category : String,
        page : Int
    ): Flow<Resource<List<Movie>>>

    suspend fun searchMovieByTitle(title: String,
                                   category : String
    ): Flow<Resource<List<Movie>>>

   suspend fun getFavorites(): Flow<List<Movie>>

    fun searchMoviesPaged(query: String,
    ): Flow<PagingData<Movie>>

    fun getAllMovies() : Flow<PagingData<Movie>>

}




