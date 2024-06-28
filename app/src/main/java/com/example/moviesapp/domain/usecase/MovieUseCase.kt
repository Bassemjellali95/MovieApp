package com.example.moviesapp.domain.usecase

import android.util.Log
import com.example.movieapp.data.moviedetails.MovieDetailsResponse
import com.example.moviesapp.common.Resource
import com.example.moviesapp.data.remote.dto.allmovies.MoviesResponse
import com.example.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository) {
    fun getMovies(
        page: Int
    ): Flow<Resource<MoviesResponse>> = flow {
        try {
            emit(Resource.Loading)
            val state = repository.getMovies(
                page
            )
            emit(Resource.Success(state))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    throwable = e
                )
            )
            Log.e("error1", e.response()?.toString() ?: "")
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server. Check your internet connection.",
                    throwable = e
                )
            )
        }
    }

    fun getMovieDetails(
        movieId: Int
    ): Flow<Resource<MovieDetailsResponse>> = flow {
        try {
            emit(Resource.Loading)
            val state = repository.getMovieDetails(
                movieId
            )
            emit(Resource.Success(state))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    throwable = e
                )
            )
            Log.e("error1", e.response()?.toString() ?: "")
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server. Check your internet connection.",
                    throwable = e
                )
            )
        }
    }
}