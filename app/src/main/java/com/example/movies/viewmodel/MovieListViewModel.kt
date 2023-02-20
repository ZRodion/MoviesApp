package com.example.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.example.movies.model.Movie
import com.example.movies.repository.MovieDetailsRepository
import com.example.movies.repository.MoviePagedListRepository
import com.example.movies.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieListViewModel(private val movieRepository: MoviePagedListRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val moviePagedList: LiveData<PagedList<Movie>> by lazy{
        movieRepository.fetchLiveMoviePageList(compositeDisposable)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }
    fun listIsEmpty(): Boolean{
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

class MovieListViewModelFactory(
    private val moviePagedListRepository: MoviePagedListRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieListViewModel(moviePagedListRepository) as T
    }
}