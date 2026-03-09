package com.example.movieapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.use_cases.PosterUseCase
import com.example.movieapp.domain.repository.MovieListRepository
import com.example.movieapp.ui.home.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel@Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val movieRepository: MovieRepository,
    private val posterUseCase: PosterUseCase
) : ViewModel() {

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()


    init {
        getFavoriteMoviesList()
    }

    private fun getFavoriteMoviesList() {
        viewModelScope.launch {
            _movieListState.update { it.copy(isLoading = true) }

            movieListRepository.getFavorites().collectLatest { favoriteList ->
                _movieListState.update {
                    it.copy(
                        favoriteMovieList = favoriteList,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun loadPoster(movie : Movie): String {
        return posterUseCase.loadPoster(movie)
    }

    fun removeFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.removeFavorite(movie.id)
        }
    }
}