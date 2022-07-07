package com.testtask.rickandmorty

import androidx.paging.PagingSource
import com.nhaarman.mockito_kotlin.mock
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.data.retrofit.model.*
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.paging.PageSource
import com.testtask.rickandmorty.domain.paging.PageSourceEpisodes
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PageSourceTest {


    @Mock
    private lateinit var remoteDataSourceCharacters: DataSource<CharactersResponseDTO, CharacterDataDTO>

    @Mock
    private lateinit var remoteDataSourceEpisodes: DataSource<EpisodesResultDTO, EpisodeDTO>

    @Mock
    private lateinit var localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity, LocationEntity>

    private lateinit var pageSource: PageSource
    private lateinit var pageSourceEpisodes: PageSourceEpisodes


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pageSource = PageSource(
            remoteDataSourceCharacters,
            localDataSource,
            StatusState.NONE,
            GenderState.NONE,
            ""
        )

        pageSourceEpisodes = PageSourceEpisodes(
            remoteDataSourceEpisodes,
            localDataSource
        )
    }

    @Test
    fun getDataById_remoteReposCharacters() {
        runBlocking {
            val charactersResponseDTO: CharactersResponseDTO = mock()
            val params: PagingSource.LoadParams<Int> = mock()
            val id = 1

            Mockito.`when`(remoteDataSourceCharacters.getDataByPagesWithFilters(id,"","","")).thenReturn(charactersResponseDTO)
            pageSource.load(params)

            Mockito.verify(remoteDataSourceCharacters, Mockito.times(1)).getDataByPagesWithFilters(id,"","","")
        }
    }


    @Test
    fun getDataById_remoteReposEpisodes() {
        runBlocking {
            val episodesResultDTO: EpisodesResultDTO  = mock()
            val params: PagingSource.LoadParams<Int> = mock()
            val id = 1

            Mockito.`when`(remoteDataSourceEpisodes.getDataByPages(id)).thenReturn(episodesResultDTO)
            pageSourceEpisodes.load(params)

            Mockito.verify(remoteDataSourceEpisodes, Mockito.times(1)).getDataByPages(id)
        }
    }

}