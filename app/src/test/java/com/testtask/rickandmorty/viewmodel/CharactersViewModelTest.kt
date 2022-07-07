package com.testtask.rickandmorty.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.paging.PageSource
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class CharactersViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    val dispatcher = UnconfinedTestDispatcher()

    private lateinit var mainViewModel: CharactersViewModel

    @Mock
    private lateinit var repositoryImpl: RepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = CharactersViewModel(repositoryImpl, UnconfinedTestDispatcher())
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun getDataTest_AppStateSuccess() {
        val resultRepos: Flow<PagingData<CharactersData>> = mock()
        val data: AppState.Success<Flow<PagingData<CharactersData>>> = AppState.Success(resultRepos)

        runTest {
            whenever(repositoryImpl.getCharactersByPage(true)).thenReturn(resultRepos)
            mainViewModel.getData(true,StatusState.NONE,GenderState.NONE,"")
            MatcherAssert.assertThat(mainViewModel.liveData.getOrAwaitValue(), `is`(data))
        }
    }


    @After
    fun tearAfter() {
        Dispatchers.resetMain()
    }
}