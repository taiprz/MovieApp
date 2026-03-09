package com.example.movieapp.DAO

import androidx.room.*
import com.example.movieapp.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    // database queries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    fun getFavorites(category: String = "FAVORITES"): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM MovieEntity WHERE id = :movieId)")
     fun exists(movieId: Int):  Flow<Int>


    @Query("DELETE FROM MovieEntity WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)

}