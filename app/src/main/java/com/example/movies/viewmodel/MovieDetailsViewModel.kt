package com.example.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.model.MovieDetails
import com.example.movies.repository.MovieDetailsRepository
import com.example.movies.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(private val movieDetailsRepository: MovieDetailsRepository, movieId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

class MovieDetailsViewModelFactory(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieDetailsRepository, movieId) as T
    }
}