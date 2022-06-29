package com.testtask.rickandmorty.data

import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO

interface DataSource<T, U, V, W> {
    suspend fun getCharactersByPages(page: Int): T
    suspend fun getCharacterDetailsById(id: Int): U
    suspend fun getEpisodesByPages(page: Int): V
    suspend fun getEpisodesDetailsById(id: Int): W

}