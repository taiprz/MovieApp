package com.example.movieapp.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.utils.Category
import com.example.movieapp.data.utils.toMovie
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.services.MovieAPI
import okio.IOException
import javax.inject.Inject

class MovieSearchPagingSource @Inject constructor(
    private val api: MovieAPI,
    private val query: String
): PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {

            val page = params.key ?: 1
            val response = api.searchByTitle(query, page)
            val movieDTO = response.results

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = response.totalPages?.let { if (it > page) page + 1 else null }

            LoadResult.Page(
                data = movieDTO?.mapNotNull { it?.toMovie(Category.POPULAR) } ?: emptyList(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}