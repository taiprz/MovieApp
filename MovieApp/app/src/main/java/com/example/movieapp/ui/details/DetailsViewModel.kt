package com.example.movieapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.use_cases.PosterUseCase
import com.example.movieapp.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val posterUseCase: PosterUseCase
) : ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailsState = _detailState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

     fun getMovie(id: Int) {
        viewModelScope.launch {
            movieRepository.getMovieById(id).collect { result ->
                when (result) {
                    is Resource.Loading -> _detailState.update {
                        it.copy(isLoading = true)
                    }
                    is Resource.Success -> _detailState.update {
                        it.copy(isLoading = false, movie = result.data)
                    }
                    is Resource.Error -> _detailState.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun addToFavorites(movie: Movie)   {
        viewModelScope.launch {
            movieRepository.addFavorite(movie)
        }
    }

     fun isFavorite (id: Int) : Flow<Boolean> {
           return movieRepository.isFavorite(id)
    }

    fun removeFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.removeFavorite(movie.id)
        }
    }

    fun loadPoster(movie : Movie): String {
        return posterUseCase.loadPoster(movie)
    }
}