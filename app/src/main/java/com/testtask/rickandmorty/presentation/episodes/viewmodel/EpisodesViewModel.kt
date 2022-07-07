package com.testtask.rickandmorty.presentation.episodes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel(){

    private var liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var liveData: LiveData<AppState> = liveDataToObserve

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            handleError(throwable)
        }


//    init {
////        liveData = getListData().cachedIn(viewModelScope).map { AppState.Success(it) }.asLiveData(viewModelScope.coroutineContext)
//        getData()
//    }


    fun getData(isOnline: Boolean) {
        liveDataToObserve.value = AppState.Loading(null)
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            getListData(isOnline).cachedIn(viewModelScope).collect {
                liveDataToObserve.postValue(AppState.Success(it))
            }
        }


    }

    private fun getListData(isOnline: Boolean): Flow<PagingData<EpisodeData>> {
        return repository.getAllEpisode(isOnline)
    }


    private fun handleError(error: Throwable) {
        liveDataToObserve.value = AppState.Error(error)
    }


}