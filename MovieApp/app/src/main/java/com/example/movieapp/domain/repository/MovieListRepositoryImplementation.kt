package com.example.movieapp.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.DAO.MovieDAO
import com.example.movieapp.data.source.MovieListPagingSource
import com.example.movieapp.data.source.MovieSearchPagingSource
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.services.MovieAPI
import com.example.movieapp.data.utils.MovieDB
import com.example.movieapp.data.utils.Resource
import com.example.movieapp.data.utils.toMovie
import com.example.movieapp.data.utils.toMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject


class MovieListRepositoryImplementation @Inject constructor(
    private val movieAPI : MovieAPI,
    private val movieDatabase : MovieDB,
    private val movieDao : MovieDAO
) : MovieListRepository {
    // here, we return a flow


    override suspend fun getMovieList(
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            // exceptions
            val movieListFromApi = try {
                movieAPI.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies."))
                return@flow
            }

            val movieEntities = movieListFromApi.results?.mapNotNull { movieDTO ->
                movieDTO?.toMovieEntity(category)
            } ?: emptyList()


            emit(
                Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
        }
    }



    override suspend fun searchMovieByTitle(
        title: String,
        category: String
    ):
            Flow<Resource<List<Movie>>> {
        return flow {
//
//            try {
//                movieAPI.searchByTitle(title)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                emit(Resource.Error(message = "Error: ${e.localizedMessage}"))
//                return@flow
//            }
//            val movies = movieAPI.searchByTitle(title).results?.mapNotNull { movieDTO ->
//                movieDTO?.toMovieEntity(category)
//            } ?: emptyList()
//
//            emit(
//                Resource.Success(
//                movies.map { it.toMovie(category) }
//            ))
//            emit(Resource.Loading(false))
        }
    }


    override suspend fun getFavorites(): Flow<List<Movie>> {

        return movieDao.getFavorites().map { entities ->
            entities.map { it.toMovie("FAVORITES") }
        }
    }

    override  fun getAllMovies(): Flow<PagingData<Movie>> {
       return Pager(config = PagingConfig(
           pageSize = MAX_ITEMS,
           prefetchDistance = PREFETCH_ITEMS),
           pagingSourceFactory = { MovieListPagingSource(movieAPI)
       }).flow
    }

    override fun searchMoviesPaged(
        query: String,
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MovieSearchPagingSource(movieAPI, query) }
        ).flow
    }

    companion object {
        const val MAX_ITEMS = 10
        const val PREFETCH_ITEMS = 3
    }
}




