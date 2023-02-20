package com.example.movies.api

import com.example.movies.model.MovieDetails
import com.example.movies.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBApi {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}