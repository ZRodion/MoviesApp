package com.example.movies.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.api.TheMovieDBApi
import com.example.movies.model.MovieDetails
import com.example.movies.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService: TheMovieDBApi,
    private val disposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = _movieDetailsResponse

    fun fetchMovieDetails(id: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            disposable.add(
                apiService.getMovieDetails(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        //success
                        { movieDetails ->
                            _movieDetailsResponse.postValue(movieDetails)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        //trouble
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            it.message?.let { it1 -> Log.e("MyTag", it1) }
                        }
                    )
            )
        } catch (e: Exception){
            Log.e("MyTag", e.message, e)
        }
    }
}