package com.example.moviesapp.di

import com.example.moviesapp.data.remote.MovieDbApi
import com.example.moviesapp.common.API_BASE_URL
import com.example.moviesapp.data.repository.MovieRepositoryImpl
import com.example.moviesapp.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private val client = OkHttpClient().newBuilder().apply {
        addInterceptor(RequestInterceptor())
    }.build()

    @Singleton
    @Provides
    fun provideRetrofitInterface(): MovieDbApi {
        return Retrofit
            .Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: MovieDbApi): MovieRepository = MovieRepositoryImpl(api)

}