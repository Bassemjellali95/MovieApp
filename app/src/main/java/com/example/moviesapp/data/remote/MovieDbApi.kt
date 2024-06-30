package com.example.moviesapp.data.remote

import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.common.DETAILS
import com.example.moviesapp.common.MOVIES
import com.example.moviesapp.common.SEARCH
import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET(MOVIES)
    suspend fun getMovies(
        @Query("page") page: Int
    ): MoviesResponse

    @GET(DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): MovieDetailsResponse

    @GET(SEARCH)
    suspend fun getMovieSearch(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviesResponse
}