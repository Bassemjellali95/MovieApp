package com.example.moviesapp.data.remote

import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse
import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.common.DETAILS
import com.example.moviesapp.common.MOVIES
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApi {

    @GET(MOVIES)
    suspend fun getMovies(
        @Query("page") page:Int,
        @Query("api_key") apiKey:String = "c9856d0cb57c3f14bf75bdc6c063b8f3"): MoviesResponse

    @GET(DETAILS)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey:String = "c9856d0cb57c3f14bf75bdc6c063b8f3"): MovieDetailsResponse
}