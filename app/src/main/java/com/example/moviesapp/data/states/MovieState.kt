package com.example.moviesapp.data.states

import com.example.moviesapp.data.remote.dto.allmovies.Movie
import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse

data class MovieState (
    val isLoading: Boolean = false,
    val movies: List<Movie>? = null,
    val error: String = ""
)