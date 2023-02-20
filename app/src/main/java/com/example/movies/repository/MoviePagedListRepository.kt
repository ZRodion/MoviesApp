package com.example.movies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.api.POST_PER_PAGE
import com.example.movies.api.TheMovieDBApi
import com.example.movies.datasource.MovieDataSource
import com.example.movies.datasource.MovieDataSourceFactory
import com.example.movies.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val movieDBApi: TheMovieDBApi) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePageList(disposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(movieDBApi, disposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }
    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState> (
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}