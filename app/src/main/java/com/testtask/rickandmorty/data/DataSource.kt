package com.testtask.rickandmorty.data

import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO

interface DataSource<T> {
    suspend fun getData(page: Int): T
    suspend fun getCharacterDetails(id: Int): CharacterDataDTO
    suspend fun getEpisodeById(id: Int): EpisodeDTO
}