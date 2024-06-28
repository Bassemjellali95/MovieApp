package com.example.moviesapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.moviesapp.common.Resource
import com.example.moviesapp.data.remote.MovieDbApi
import com.example.moviesapp.data.states.MovieDetailsState
import com.example.moviesapp.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
    private val api: MovieDbApi
) : ViewModel() {

    private val _moviesState = mutableStateOf(MovieDetailsState())
    val moviesState: State<MovieDetailsState> = _moviesState

    val moviesList = Pager(PagingConfig(1)) {
        MoviePagingSource(api)
    }.flow.cachedIn(viewModelScope)


    fun getMovieDetails(
        movieId: Int
    ) {
        viewModelScope.launch {
            useCase.getMovieDetails(
                movieId
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _moviesState.value = MovieDetailsState(movieDetails = result.data)
                        Log.e("success", result.data.toString())
                    }

                    is Resource.Error -> {
                        val errorMsg = if (result.data != null) {
                            result.message ?: "An unexpected error occurred"
                        } else {
                            try {
                                result.throwable.let { error ->
                                    val errorBody =
                                        (error as HttpException).response()?.errorBody()?.string()
                                    JSONObject(errorBody ?: "").getString("message")
                                } ?: "An unexpected error occurred"
                            } catch (e: Exception) {
                                "An unexpected error occurred"
                            }
                        }
                        _moviesState.value = MovieDetailsState(error = errorMsg)
                        Log.e("error", errorMsg)
                    }

                    is Resource.Loading -> {
                        _moviesState.value = MovieDetailsState(isLoading = true)
                    }
                }
            }
        }
    }
}
