package com.testtask.rickandmorty.presentation.character.viewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    private var liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    var liveData: LiveData<AppState> = liveDataToObserve

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            handleError(throwable)
        }


    init {


//        liveData = getListData().cachedIn(viewModelScope).map { AppState.Success(it) }.asLiveData(viewModelScope.coroutineContext)

//        getData(false)
    }


    fun getData(
        isOnline: Boolean,
        state: StatusState,
        genderState: GenderState,
        searchText: String
    ) {
        liveDataToObserve.value = AppState.Loading(null)
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            getListData(isOnline,state,genderState,searchText).cachedIn(viewModelScope).collectLatest {
                liveDataToObserve.postValue(AppState.Success(it))
            }
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