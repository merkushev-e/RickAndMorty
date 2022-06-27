package com.testtask.rickandmorty.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.testtask.rickandmorty.data.retrofit.BaseInterceptor
import com.testtask.rickandmorty.data.retrofit.RMApi
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule {

    companion object {
        private const val BASE_URL_LOCATIONS = "https://rickandmortyapi.com/api/"
    }

    @Reusable
    @Provides
    fun provideRetrofitApi(): RMApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(BaseInterceptor.interceptor))
            .build()
            .create(RMApi::class.java)
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }


}