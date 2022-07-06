package com.testtask.rickandmorty.data.retrofit

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO

class RemoteDataSourceEpisodes(private val api: RMApi) :
    DataSource<EpisodesResultDTO, EpisodeDTO> {

    override suspend fun getDataByPages(page: Int): EpisodesResultDTO {
        return api.getAllEpisode(page)
    }

    override suspend fun getDataById(id: Int): EpisodeDTO {
        return api.getEpisodeById(id)
    }

    override suspend fun getDataByPagesWithFilters(
        page: Int,
        status: String,
        gender: String,
        name: String
    ): EpisodesResultDTO {
        TODO("Not yet implemented")
    }
}