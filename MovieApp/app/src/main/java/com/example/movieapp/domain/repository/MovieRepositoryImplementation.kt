package com.example.movieapp.domain.repository

import com.example.movieapp.DAO.MovieDAO
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.services.MovieAPI
import com.example.movieapp.data.utils.Resource
import com.example.movieapp.data.utils.toMovie
import com.example.movieapp.data.utils.toMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class MovieRepositoryImplementation @Inject constructor(
    private val movieAPI: MovieAPI, private val movieDao: MovieDAO
) : MovieRepository {

    override suspend fun getMovieById(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading(true))
        try {
            val movieFromApi = movieAPI.detailsById(id)
            val movie = movieFromApi.toMovie("details")
            emit(Resource.Success(movie))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error."))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.localizedMessage}"))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addFavorite(movie: Movie) {
        movieDao.insert(movie.toMovieEntity("FAVORITES"))
    }

    override suspend fun removeFavorite(movieId: Int) {
        movieDao.deleteById(movieId)
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.exists(movieId).map { it > 0 }
    }
}