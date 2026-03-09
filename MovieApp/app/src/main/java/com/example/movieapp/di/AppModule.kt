package com.example.movieapp.di

import android.app.Application
import androidx.room.Room
import com.example.movieapp.DAO.MovieDAO
import com.example.movieapp.services.MovieAPI
import com.example.movieapp.utils.MovieDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesMovieApi() : MovieAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieAPI.BASE_URL)
            .client(client)
            .build()
            .create(MovieAPI::class.java)
    }


    @Provides
    @Singleton
    fun providesMovieDB(app: Application) : MovieDB {
        return Room.databaseBuilder(
            app,
            MovieDB::class.java,
            "moviedb.db"
        ).build()
    }

    @Provides
    fun provideMovieDAO(database: MovieDB): MovieDAO {
        return database.movieDao
    }
}