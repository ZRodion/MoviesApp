package com.example.movies.model

import com.google.gson.annotations.SerializedName
import java.nio.file.Path

data class Movie(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)