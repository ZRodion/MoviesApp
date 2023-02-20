package com.example.movies.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    companion object{
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val END_OF_LIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Error")
            END_OF_LIST = NetworkState(Status.FAILED, "End of list")
        }
    }
}