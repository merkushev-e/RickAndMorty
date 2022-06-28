package com.testtask.rickandmorty.presentation.character.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private var liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var liveData: LiveData<AppState> = liveDataToObserve
    private val resultList = mutableListOf<EpisodeData>()


    fun getEpisodesList(charactersData: CharactersData) {
        liveDataToObserve.value = AppState.Loading(null)
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            charactersData.episode.forEach {
                val episodeId = it.split("/")[5].toInt()
                resultList.add(repository.getEpisodeById(episodeId))
            }
            liveDataToObserve.postValue(AppState.SuccessDetails(resultList))
        }

    }


    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            handleError(throwable)
        }

    private fun handleError(error: Throwable) {
        liveDataToObserve.value = AppState.Error(error)
    }

}