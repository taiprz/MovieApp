package com.example.movieapp.DTOs


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListDTO(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<MovieDTO?>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)