package com.testtask.rickandmorty.presentation.character.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var liveData: LiveData<AppState> = liveDataToObserve

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }


    fun getData(
        isOnline: Boolean,
        state: StatusState,
        genderState: GenderState,
        searchText: String
    ) {
        liveDataToObserve.value = AppState.Loading(null)
        viewModelScope.launch(defaultDispatcher + coroutineExceptionHandler) {
            liveDataToObserve.postValue(AppState.Success(getListData(isOnline, state, genderState, searchText)))
//            getListData(isOnline, state, genderState, searchText).cachedIn(viewModelScope)
//                .collectLatest {
//                    liveDataToObserve.postValue(AppState.Success(it))
//                }
        }


    }


    private fun getListData(
        isOnline: Boolean, state: StatusState,
        genderState: GenderState,
        searchText: String
    ): Flow<PagingData<CharactersData>> {
        return repository.getCharactersByPage(
            isOnline,
            state,
            genderState,
            searchText
        )
    }


    private fun handleError(error: Throwable) {
        liveDataToObserve.value = AppState.Error(error)
    }

}