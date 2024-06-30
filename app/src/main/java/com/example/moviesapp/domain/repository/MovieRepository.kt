package com.example.moviesapp.domain.repository

import com.example.movieapp.data.moviedetails.MovieDetailsResponse

interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
}