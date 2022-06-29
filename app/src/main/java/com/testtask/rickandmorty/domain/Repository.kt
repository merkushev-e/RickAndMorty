package com.testtask.rickandmorty.domain

import androidx.paging.PagingData
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.EpisodesResultData
import com.testtask.rickandmorty.domain.model.LocationData
import kotlinx.coroutines.flow.Flow


interface Repository{
     fun getCharactersByPage() : Flow<PagingData<CharactersData>>
     fun saveData(appState: AppState)
     suspend fun getCharacterDetails(id: Int): CharactersData
     suspend fun getEpisodeById(id: Int): EpisodeData
     fun getAllEpisode(): Flow<PagingData<EpisodeData>>
     fun getAllLocations(): Flow<PagingData<LocationData>>
     fun getLocationById(id: Int): LocationData



}