package com.example.moviesapp.domain.repository

import androidx.paging.PagingSource
import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.data.remote.dto.allmovies.Movie
import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse
import com.example.moviesapp.ui.viewmodel.MoviePagingSource

interface MovieRepository {
    suspend fun getMovies(page:Int): MoviesResponse
    suspend fun getMovieDetails(movieId:Int): MovieDetailsResponse
}