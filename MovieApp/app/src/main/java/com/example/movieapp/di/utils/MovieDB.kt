package com.example.movieapp.di.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.DAO.MovieDAO
import com.example.movieapp.entities.MovieEntity


// database where movies are stored
@Database (
    entities = [MovieEntity::class],
    version = 1
)
abstract class MovieDB : RoomDatabase() {
    abstract val movieDao : MovieDAO
}