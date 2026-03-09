package com.example.movieapp.DTOs


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListDTO(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<MovieDTO?>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)