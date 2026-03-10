package com.example.movieapp.data.services

import com.example.movieapp.DTOs.MovieDTO
import com.example.movieapp.DTOs.MovieListDTO
import com.example.movieapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieAPI {

    // api queries
    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page : Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDTO


    @GET("movie/{id}")
    suspend fun detailsById(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDTO

       @GET("search/movie")
       suspend fun searchByTitle(
       @Query("query") title: String,
       @Query("page") page : Int,
       @Query("api_key") apiKey : String = API_KEY
        ): MovieListDTO

    companion object {
        const val BASE_URL =  "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.API_KEY

    }
}