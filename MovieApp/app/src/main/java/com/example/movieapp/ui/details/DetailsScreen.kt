package com.example.movieapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.use_cases.PosterUseCase
import com.example.movieapp.ui.theme.Parchment
import com.example.movieapp.ui.theme.PetalFrost

@Composable
fun DetailsView(
    detailsViewModel: DetailViewModel = hiltViewModel(),
    movieId: Int,
    navController: NavController
) {
    LaunchedEffect(movieId) {
        detailsViewModel.getMovie(movieId)
    }

    val detailState by detailsViewModel.detailsState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {

        if (detailState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        detailState.movie?.let { movie ->
            MovieDetails(movie, navController, detailsViewModel)
        }
    }
}

@Composable
fun MovieDetails(
    movie: Movie,
    navController: NavController,
    detailsViewModel: DetailViewModel
) {
    val isFavorite = detailsViewModel.isFavorite(movie.id).collectAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Parchment)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Box(
            modifier = Modifier.aspectRatio(2 / 3f)
        ) {
            AsyncImage(
                model = detailsViewModel.loadPoster(movie),
                contentDescription = "Movie poster",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(
            modifier = Modifier.width(5.dp)
        )

        Text(
            text = movie.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        Text(
            movie.overview,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp)) {
                AsyncImage(
                    model = R.drawable.ic_calendar,
                    contentDescription = "Calendar icon",
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Text1(movie.releaseDate)

            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Text("|")
            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Box(
                modifier = Modifier.size(24.dp)
            ) {
                AsyncImage(
                    model = R.drawable.ic_star,
                    contentDescription = "Star icon",
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Text1(movie.voteAverage.toString())
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(navController)

            Spacer(modifier = Modifier.width(16.dp))
            if (!isFavorite.value) {

                FavoriteButton(
                    dialogTitle = "Save movie",
                    dialogText = "Add to favorites?",
                    onDismissRequest = {},
                    onConfirmation = { detailsViewModel.addToFavorites(movie) },
                    isFavorite = isFavorite
                )
            } else {
                FavoriteButton(
                    dialogTitle = "Delete movie",
                    dialogText = "Delete from favorites?",
                    onDismissRequest = {},
                    onConfirmation = { detailsViewModel.removeFavorite(movie) },
                    isFavorite = isFavorite
                )
            }
        }
    }
}

@Composable
fun BackButton(navController: NavController) {
    IconButton(
        onClick = {
            navController.popBackStack()
        },
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "return",
            tint = Color.Black
        )
    }
}

@Composable
fun FavoriteButton(
    dialogTitle: String?,
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    isFavorite: State<Boolean>
) {

    var showDialog by remember { mutableStateOf(false) }
    val iconTint = if (isFavorite.value) PetalFrost else Color.Black
    val dialogIconTint = if (isFavorite.value) Color.Black else Color.Red

    Row(
        modifier = Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        IconButton(onClick = { showDialog = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = "favorite",
                tint = iconTint
            )
        }

        if (showDialog) {
            AlertDialog(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_broken_heart),
                        contentDescription = "delete",
                        tint = dialogIconTint,
                        modifier = Modifier
                            .size(25.dp)
                    )
                },

                title = {
                    dialogTitle?.let {
                        Text(
                            text = it,
                            color = Color.Black
                        )
                    }
                },

                text = {
                    Text(
                        text = dialogText,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                },

                onDismissRequest = {
                    showDialog = false
                    onDismissRequest()
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onConfirmation()
                        }
                    ) {
                        Text("Confirm", color = Color.Red)
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onDismissRequest()
                        }
                    ) {
                        Text("Dismiss", color = Color.Black)
                    }
                }
            )
        }
    }
}
@Composable
private fun Text1(
    text: String, modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.DarkGray,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
    )
}


@Preview(showBackground = true)
@Composable
fun Prvw() {
//    details(
//        movie = Movie(
//            id = 2,
//            title = "Preview Movie",
//            overview = " This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data. This is just preview data.",
//            adult = false,
//            backdropPath = "/qhyyyWrHUbl6QG4udAJj17CBa5.jpg",
//            originalLanguage = "en",
//            originalTitle = "Preview Movie",
//            popularity = 0.0,
//            posterPath = "/qhyyyWrHUbl6QG4udAJj17CBa5.jpg",
//            releaseDate = "2024-01-01",
//            voteAverage = 5.365,
//            voteCount = 100,
//            video = false,
//            category = "POPULAR",
//            genreIds = listOf("Action, Suspense")
//        )
//    )
}
