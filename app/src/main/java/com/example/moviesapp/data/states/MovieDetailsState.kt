package com.example.moviesapp.data.states

import com.example.movieapp.data.moviedetails.MovieDetailsResponse

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetailsResponse? = null,
    val error: String = ""
)
