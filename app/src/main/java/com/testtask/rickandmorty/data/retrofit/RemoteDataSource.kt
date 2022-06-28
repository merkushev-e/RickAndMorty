package com.testtask.rickandmorty.data.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource: DataSource<CharactersResponseDTO> {

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL_LOCATIONS)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(createOkHttpClient(BaseInterceptor.interceptor))
        .build().create(RMApi::class.java)


    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }


    override suspend fun getData(page: Int): CharactersResponseDTO {
        return api.getAllCharacters(page)
    }

    override suspend fun getCharacterDetails(id: Int): CharacterDataDTO {
        return api.getCharacter(id)
    }


    override suspend fun getEpisodeById(id: Int): EpisodeDTO {
        return api.getEpisodeById(id)
    }


    companion object {
        private const val BASE_URL_LOCATIONS = "https://rickandmortyapi.com/api/"
    }



}