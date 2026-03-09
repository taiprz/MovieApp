package com.example.movieapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.movieapp.ui.home.HomeViewModel
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.utils.Screen
import com.example.movieapp.ui.home.MovieListEvents
import com.example.movieapp.data.utils.Category
import com.example.movieapp.ui.theme.BlueStale
import com.example.movieapp.ui.theme.CoolSteel
import com.example.movieapp.ui.theme.DustGrey
import com.example.movieapp.ui.theme.Parchment
import com.example.movieapp.ui.theme.PetalFrost

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val movies = homeViewModel.movies.collectAsLazyPagingItems()
    MoviesList(movies,  navController = navController, homeViewModel = homeViewModel)
    val movieState by homeViewModel.movieListState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Parchment)
            .padding(16.dp)
    ) {

        SearchBar(homeViewModel)

        Spacer(modifier = Modifier.height(16.dp))

//        MovieList(
//            movies = movieState.popularMovieList,
//            navController = navController,
//            homeViewModel = homeViewModel
//        )
        MoviesList(movies,  navController = navController, homeViewModel = homeViewModel)
    }
}


@Composable
fun SearchBar(homeViewModel: HomeViewModel) {

    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            if (newText.isEmpty()) {
                homeViewModel.onEvent(MovieListEvents.Paginate(Category.POPULAR))
            } else {
                homeViewModel.onEvent(MovieListEvents.Search(newText))
            }
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search movies...") },
        singleLine = true,
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
    movies: List<Movie>,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {


    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie, navHostController = navController, homeViewModel = homeViewModel
            )
        }
    }

    LaunchedEffect(listState, movies) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index != null && index >= movies.lastIndex - 1) {
                    homeViewModel.onEvent(MovieListEvents.Paginate(Category.POPULAR))
                }
            }
    }
}

@Composable
fun MoviesList(movies : LazyPagingItems<Movie>,
               navController: NavHostController,
               homeViewModel: HomeViewModel) {
    LazyColumn() {
        items(movies.itemCount) {
            movies[it]?. let { movie ->
                ItemList( movie = movie, navHostController = navController, homeViewModel = homeViewModel
                )
            }
        }
    }
}

@Composable
fun ItemList(movie: Movie,
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

@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController,
    homeViewModel: HomeViewModel
) {
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




