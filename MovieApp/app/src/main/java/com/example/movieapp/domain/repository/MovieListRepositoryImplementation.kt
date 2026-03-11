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


// TODO: CONVERT NOT SUSPEND FUNCTIONS TO SUSPEND WHEN NEEDED
class MovieListRepositoryImplementation @Inject constructor(
    private val movieAPI : MovieAPI,
    private val movieDao : MovieDAO
) : MovieListRepository {



    override suspend fun getFavorites(): Flow<List<Movie>> {

        return movieDao.getFavorites().map { entities ->
            entities.map { it.toMovie("FAVORITES") }
        }
    }

    override  fun getAllMovies(): Flow<PagingData<Movie>> {
       return Pager(config = PagingConfig(
           pageSize = MAX_ITEMS,
           prefetchDistance = PREFETCH_ITEMS),
           pagingSourceFactory = { MovieListPagingSource(movieAPI) }
       ).flow
    }

    override fun searchMoviesPaged(
        query: String,
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MovieSearchPagingSource(movieAPI, query) }
        ).flow
    }

    private companion object {
        const val MAX_ITEMS = 10
        const val PREFETCH_ITEMS = 3
    }
}




