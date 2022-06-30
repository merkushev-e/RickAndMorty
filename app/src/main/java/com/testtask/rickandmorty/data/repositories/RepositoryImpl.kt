package com.testtask.rickandmorty.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.*
import com.testtask.rickandmorty.domain.paging.PageSource
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.PageSourceLocation
import com.testtask.rickandmorty.domain.paging.PageSourceEpisodes
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.utils.toCharactersData
import com.testtask.rickandmorty.utils.toEpisodeData
import com.testtask.rickandmorty.utils.toLocationData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val remoteDataSourceCharacters: DataSource<CharactersResponseDTO, CharacterDataDTO>,
    private val remoteDataSourceEpisode: DataSource<EpisodesResultDTO, EpisodeDTO>,
    private val remoteDataSourceLocations: DataSource<LocationsResultDTO, LocationDTO>
) : Repository {
    override fun getCharactersByPage(): Flow<PagingData<CharactersData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSource(remoteDataSourceCharacters) }
        ).flow
    }


    override fun saveData(appState: AppState) {
        TODO("Not yet implemented")

    }

    override suspend fun getCharacterDetails(id: Int): CharactersData {
        return remoteDataSourceCharacters.getDataById(id).toCharactersData()
    }

    override suspend fun getEpisodeById(id: Int): EpisodeData {
        return remoteDataSourceEpisode.getDataById(id).toEpisodeData()
    }

    override fun getAllEpisode(): Flow<PagingData<EpisodeData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSourceEpisodes(remoteDataSourceEpisode) }
        ).flow
    }

    override fun getAllLocations(): Flow<PagingData<LocationData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSourceLocation(remoteDataSourceLocations) }
        ).flow
    }

    override suspend fun getLocationById(id: Int): LocationData {
        return remoteDataSourceLocations.getDataById(id).toLocationData()
    }

}