package com.example.movies.repository

import androidx.lifecycle.LiveData
import com.example.movies.api.TheMovieDBApi
import com.example.movies.datasource.MovieDetailsNetworkDataSource
import com.example.movies.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBApi) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(disposable: CompositeDisposable, id: Int): LiveData<MovieDetails>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, disposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(id)

        return movieDetailsNetworkDataSource.movieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> = movieDetailsNetworkDataSource.networkState
}