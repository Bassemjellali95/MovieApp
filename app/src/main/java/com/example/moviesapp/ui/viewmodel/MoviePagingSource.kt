package com.example.moviesapp.ui.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.example.moviesapp.data.remote.MovieDbApi
import com.example.moviesapp.data.remote.dto.allmovies.Movie

class MoviePagingSource(
    private val movieApi: MovieDbApi,
    private val query: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = if (query.isNullOrEmpty()) {
                movieApi.getMovies(page)
            } else {
                movieApi.getMovieSearch(query, page)
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page.plus(1)
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}