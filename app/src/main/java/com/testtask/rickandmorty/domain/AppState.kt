package com.testtask.rickandmorty.domain

import androidx.paging.PagingData
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData


sealed class AppState{
    data class Success(val data: PagingData<CharactersData>?) : AppState()
    data class SuccessDetails(val data: MutableList<EpisodeData>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
