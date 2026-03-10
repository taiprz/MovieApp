package com.example.movieapp.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.size.Size
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.data.utils.Screen
import com.example.movieapp.ui.theme.Parchment
import com.example.movieapp.ui.theme.PetalFrost


@Composable
fun FavoritesView(
    favoritesViewModel: FavoritesViewModel,
    navController: NavHostController
) {
    val movieState by favoritesViewModel.movieListState.collectAsState()
    var editMode by remember { mutableStateOf(false) }

    if (editMode) {
        EditMode(
            favoritesViewModel = favoritesViewModel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Parchment)
            .padding(16.dp)
    ) {

        Header(
            movies = movieState.favoriteMovieList,
            navController = navController,
            favoritesViewModel = favoritesViewModel,
            editMode = editMode,
            onEditClick = { editMode = !editMode })

        Spacer(modifier = Modifier.height(16.dp))

        when {
            movieState.isLoading -> {
                CircularProgressIndicator()
            }

            movieState.favoriteMovieList.isEmpty() -> {
                EmptyListView(navController)
            }

            else -> {
                FavMovieList(
                    movies = movieState.favoriteMovieList,
                    navController = navController,
                    editMode = editMode,
                    favoritesViewModel = favoritesViewModel
                )
            }
        }
    }
}

@Composable
fun Header(
    movies: List<Movie>,
    favoritesViewModel: FavoritesViewModel,
    navController: NavHostController,
    onEditClick: () -> Unit,
    editMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Your Favorites",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,

            )

        IconButton(
            onClick = onEditClick,
            shape = CircleShape,
            modifier = Modifier
                .background(
                    color = PetalFrost,
                    shape = CircleShape
                )
                .size(37.dp),


            ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "Edit button",
                modifier = Modifier.size(20.dp),
                tint = if (editMode) Parchment else Color.Black


            )
        }
    }
}

@Composable
fun FavMovieList(
    movies: List<Movie>,
    navController: NavHostController,
    editMode: Boolean,
    favoritesViewModel: FavoritesViewModel
) {



    if (movies.isEmpty()) {
        EmptyListView(navController)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                navController,
                editMode,
                favoritesViewModel = favoritesViewModel
            )
        }
    }
}

@Composable
fun EmptyListView(navController: NavController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Parchment),
        contentAlignment = Alignment.Center){

        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp),
            model = R.drawable.ic_sad,
            contentDescription = "sad face"
        )


        Text(text = "No movies saved yet",
            fontWeight = FontWeight.Bold,
            )

            Button(
                colors =
                    ButtonColors(
                        containerColor = PetalFrost,
                        contentColor = Color.White,
                        disabledContentColor = PetalFrost,
                        disabledContainerColor = PetalFrost
                    ),
                onClick = { navController.popBackStack()},

            ) {
                Text("Discover movies")
            }
        }
    }
}


@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController,
    editMode: Boolean,
    favoritesViewModel: FavoritesViewModel
) {

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                navHostController.navigate("${Screen.Details.route}/${movie.id}")
            },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box() {
            AsyncImage(
                model = favoritesViewModel.loadPoster(movie),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
            )

            if (editMode) {
                IconButton(
                    modifier = Modifier
                        .size(20.dp),
                    onClick = {
                        favoritesViewModel.removeFavorite(movie)
                    },
                ) {
                    AsyncImage(
                        model = R.drawable.ic_remove,
                        contentDescription = "Delete"
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditMode(
    favoritesViewModel: FavoritesViewModel
) {
    val movieState by favoritesViewModel.movieListState.collectAsState()
}

@Composable
fun EditModePrw() {
    Box(
        modifier = Modifier
            .background(color = Parchment)
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .background(Color.Blue)
                .size(75.dp, 150.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(25.dp)
            ) {
                IconButton(
                    onClick = {}
                ) {
                    AsyncImage(
                        model = R.drawable.ic_remove,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {


}


