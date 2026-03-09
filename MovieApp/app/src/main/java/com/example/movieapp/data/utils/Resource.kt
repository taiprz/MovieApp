package com.example.movieapp.data.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    // Data will be a list of Movies, if we succees to get them, this will be used
    class Success<T>(data: T?): Resource<T>(data)
    // In case an error occurs, we will send a message
    class Error<T> (message: String, data: T? = null) : Resource<T>(data, message)
    // after it charges, Loading will be set to FALSE
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}

