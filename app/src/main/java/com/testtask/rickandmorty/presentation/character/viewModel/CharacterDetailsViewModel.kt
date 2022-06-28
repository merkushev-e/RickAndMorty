package com.testtask.rickandmorty.presentation.character.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel(){

    private var liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var liveData: LiveData<AppState> = liveDataToObserve


    fun getCharacterDetail(id: Int){
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            liveDataToObserve.postValue(AppState.SuccessDetails(repository.getCharacterDetails(id)))
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