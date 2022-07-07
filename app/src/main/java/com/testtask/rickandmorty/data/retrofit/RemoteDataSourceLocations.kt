package com.testtask.rickandmorty.data.retrofit

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.data.retrofit.model.LocationDTO
import com.testtask.rickandmorty.data.retrofit.model.LocationsResultDTO

class RemoteDataSourceLocations(private val api: RMApi): DataSource<LocationsResultDTO, LocationDTO> {
    override suspend fun getDataByPages(page: Int): LocationsResultDTO {
       return api.getAllLocation(page)
    }

    override suspend fun getDataById(id: Int): LocationDTO {
       return api.getLocation(id)
    }

    override suspend fun getDataByPagesWithFilters(
        page: Int,
        status: String,
        gender: String,
        name: String
    ): LocationsResultDTO {
        TODO("Not yet implemented")
    }

}