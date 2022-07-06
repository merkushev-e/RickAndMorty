package com.testtask.rickandmorty.domain

import androidx.paging.PagingData
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.EpisodesResultData
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState
import kotlinx.coroutines.flow.Flow


interface Repository {
    fun getCharactersByPage(
         isOnline: Boolean, status: StatusState = StatusState.NONE,
         gender: GenderState = GenderState.NONE,
         name: String = ""
    ): Flow<PagingData<CharactersData>>

    suspend fun saveData(appState: AppState)
    suspend fun getCharacterDetails(id: Int): CharactersData
    suspend fun getEpisodeById(id: Int): EpisodeData
    fun getAllEpisode(isOnline: Boolean): Flow<PagingData<EpisodeData>>
    fun getAllLocations(isOnline: Boolean): Flow<PagingData<LocationData>>
    suspend fun getLocationById(id: Int): LocationData


}