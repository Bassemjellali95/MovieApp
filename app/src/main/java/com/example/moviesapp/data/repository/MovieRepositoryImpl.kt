package com.example.moviesapp.data.repository

import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.data.remote.MovieDbApi
import com.example.moviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieDbApi: MovieDbApi) :
    MovieRepository {
    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse {
        return movieDbApi.getMovieDetails(movieId)
    }

}