package com.testtask.rickandmorty.data.retrofit

import com.testtask.rickandmorty.data.retrofit.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RMApi {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int? = null
    ): CharactersResponseDTO

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDataDTO

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") episodeId: Int): EpisodeDTO

    @GET("episode")
    suspend fun getAllEpisode(@Query("page") page: Int? = null): EpisodesResultDTO

    @GET("location")
    suspend fun getAllLocation(@Query("page") page: Int? = null): LocationsResultDTO

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") locationId: Int): LocationDTO

}