package com.example.moviesapp.data.repository

import androidx.paging.PagingSource
import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.data.remote.MovieDbApi
import com.example.moviesapp.data.remote.dto.allmovies.Movie
import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse
import com.example.moviesapp.domain.repository.MovieRepository
import com.example.moviesapp.ui.viewmodel.MoviePagingSource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieDbApi: MovieDbApi) :
    MovieRepository {
    override suspend fun getMovies(page: Int): MoviesResponse {
        return movieDbApi.getMovies(page)
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse {
        return movieDbApi.getMovieDetails(movieId)
    }

}