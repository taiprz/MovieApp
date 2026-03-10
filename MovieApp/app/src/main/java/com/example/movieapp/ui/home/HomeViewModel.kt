package com.example.movieapp.ui.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.use_cases.PosterUseCase
import com.example.movieapp.domain.repository.MovieListRepository
import com.example.movieapp.data.utils.Category
import com.example.movieapp.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val posterUseCase: PosterUseCase
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = movieListRepository.getAllMovies()
    private val _moviesFound = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val moviesFound: StateFlow<PagingData<Movie>> = _moviesFound.asStateFlow()
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
//        getPopularMoviesList()
        observeSearch()
    }


    fun onEvent(event: MovieListEvents) {
        when (event) {

//            is MovieListEvents.Paginate -> {
//                if (event.category == Category.POPULAR) {
//                    getPopularMoviesList()
//                }
//            }


            is MovieListEvents.Search -> {
                _searchText.update { event.query }
            }

            else -> {}
        }
    }


    private fun getPopularMoviesList() {

        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                Category.POPULAR, movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + popularList.shuffled(),
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadPoster(movie: Movie): String {
        return posterUseCase.loadPoster(movie)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            _searchText.debounce(500).collectLatest { query ->
                if (query.isNotBlank()) {
                    pagedResultsFromSearch(query)
                }
            }
        }
    }

    private fun resultsFromSearchList(title: String) {
        viewModelScope.launch {
            _movieListState.update { it.copy(isLoading = true) }


            movieListRepository.searchMovieByTitle(title, category = Category.POPULAR)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _movieListState.update { it.copy(isLoading = false) }
                        }

                        is Resource.Loading -> {
                            _movieListState.update { it.copy(isLoading = result.isLoading) }
                        }

                        is Resource.Success -> {
                            result.data?.let { resultList ->
                                _movieListState.update {
                                    it.copy(
                                        popularMovieList = resultList,
                                        popularMovieListPage = 1,
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

         private fun pagedResultsFromSearch(title: String) {
             viewModelScope.launch {
                 movieListRepository.searchMoviesPaged(title)
                     .collectLatest { pagingData ->
                         _moviesFound.value = pagingData
                     }
             }
    }
}