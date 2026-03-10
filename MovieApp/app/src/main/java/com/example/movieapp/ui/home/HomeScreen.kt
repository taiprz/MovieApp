package com.example.movieapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.utils.Screen
import com.example.movieapp.data.utils.Category
import com.example.movieapp.ui.theme.DustGrey
import com.example.movieapp.ui.theme.Parchment
import com.example.movieapp.ui.theme.PetalFrost

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val movies = homeViewModel.movies.collectAsLazyPagingItems()
    val moviesFound = homeViewModel.moviesFound.collectAsLazyPagingItems()

    var searchText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Parchment)
            .padding(16.dp)
    ) {

        SearchBar(
            searchText = searchText,
            onSearchTextChange = {
                searchText = it
                homeViewModel.onEvent(MovieListEvents.Search(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchText.isEmpty()) {
            MovieList(movies = movies, navController = navController, homeViewModel = homeViewModel)
        } else {
            MovieList(movies = moviesFound, navController = navController, homeViewModel = homeViewModel)
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { onSearchTextChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search movies...") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PetalFrost,
            unfocusedContainerColor = PetalFrost,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun MovieList(
    movies: LazyPagingItems<Movie>,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    movie = movie,
                    navHostController = navController,
                    homeViewModel = homeViewModel
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MovieItem(movie: Movie,
             navHostController: NavHostController,
             homeViewModel: HomeViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = DustGrey)
        .clickable {
            navHostController.navigate("${Screen.Details.route}/${movie.id}")
        }
        .padding(8.dp)) {

        AsyncImage(
            model = homeViewModel.loadPoster(movie) ,
            contentDescription = "Movie poster",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = movie.overview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}





