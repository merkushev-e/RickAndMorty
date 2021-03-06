package com.testtask.rickandmorty.domain

import androidx.paging.PagingData
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.LocationData


sealed class AppState{
    data class Success<T>(val data: T?) : AppState()
    data class SuccessDetails(val data: MutableList<EpisodeData>) : AppState()
    data class SuccessDetailsCharacter(val data: MutableList<CharactersData>) : AppState()
    data class SuccessDetailsLocation(val data: LocationData) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
