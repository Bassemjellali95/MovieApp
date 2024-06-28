package com.example.moviesapp.data.remote.dto.allmovies

data class MoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)