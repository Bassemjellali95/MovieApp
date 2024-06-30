package com.example.moviesapp.ui.viewmodel

import android.annotation.SuppressLint
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
    private val api: MovieDbApi
) : ViewModel() {

    private val _moviesState = mutableStateOf(MovieDetailsState())
    val moviesState: State<MovieDetailsState> = _moviesState

    private val _search = MutableStateFlow("")

    private val search = _search.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "",
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    @SuppressLint("CheckResult")
    val moviesList = search.debounce(300.milliseconds).flatMapLatest { query ->
        Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            MoviePagingSource(
                movieApi = api,
                query = query
            )
        }.flow.cachedIn(viewModelScope)
    }

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
                                        (error as HttpException).response()?.errorBody()
                                            ?.string()
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

    fun setSearch(query: String) {
        _search.value = query
    }

}