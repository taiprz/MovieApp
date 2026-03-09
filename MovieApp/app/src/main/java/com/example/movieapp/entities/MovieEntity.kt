package com.example.movieapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(

    val adult: Boolean,
    val backdropPath: String,
    val genreIds: String,
    @PrimaryKey
    val id: Int,
    val originalLanguage : String,
    val originalTitle : String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title : String,
    val voteAverage : Double,
    val voteCount: Int,
    val video : Boolean,
    val category: String
)