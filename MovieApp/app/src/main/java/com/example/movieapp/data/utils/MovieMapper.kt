package com.example.movieapp.data.utils
import com.example.movieapp.DTOs.MovieDTO
import com.example.movieapp.entities.MovieEntity
import com.example.movieapp.domain.model.Movie
import kotlin.collections.emptyList
import kotlin.text.split

fun MovieDTO.toMovieEntity(
    category: String
) : MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdropPath = backdropPath?: "",
        originalLanguage = originalLanguage ?: "",
        overview = overview ?: "" ,
        posterPath = posterPath ?: "",
        releaseDate = releaseDate?: "",
        title = title ?: "",
        voteAverage = voteAverage?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = voteCount ?: 0,
        video = video ?: false,
        id = id ?: -1,
        originalTitle = originalTitle ?: "",
        category = category,
        genreIds  = genreIds?.joinToString(",") ?: ""
    )
}



fun MovieEntity.toMovie(
    category: String
) : Movie {
    return Movie(
        adult = adult,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        popularity = popularity,
        voteCount = voteCount,
        video = video,
        id = id,
        originalTitle = originalTitle,
        category = category,
        genreIds = genreIds.split(",").filter { it.isNotEmpty() })
}


fun MovieDTO.toMovie(
    category: String
) : Movie {
    return Movie(
        adult = adult ?: false,
        backdropPath = backdropPath ?: "",
        originalLanguage = originalLanguage ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        releaseDate = releaseDate ?: "",
        title = title ?: "",
        voteAverage = voteAverage ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = voteCount ?: 0,
        video = video ?: false,
        id = id ?: -1,
        originalTitle = originalTitle ?: "",
        category = category,
        genreIds = genreIds?.map { it.toString() } ?: emptyList()
    )
}

fun Movie.toMovieEntity (
    category: String
) : MovieEntity {
    return MovieEntity(
        adult = adult,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        popularity = popularity,
        voteCount = voteCount,
        video = video,
        id = id,
        originalTitle = originalTitle,
        category = category,
        genreIds = genreIds.joinToString(",") ?: ""
    )
}


