package com.testtask.rickandmorty.data

import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO

interface DataSource<T, U> {
    suspend fun getDataByPages(page: Int): T
    suspend fun getDataById(id: Int): U

}