package com.example.movies.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movies.api.TheMovieDBApi
import com.example.movies.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val api: TheMovieDBApi,
    private val disposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(api, disposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}