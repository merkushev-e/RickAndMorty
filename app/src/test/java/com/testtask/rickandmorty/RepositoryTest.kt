package com.testtask.rickandmorty

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.data.retrofit.model.*
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RepositoryTest {

    private lateinit var repository: RepositoryImpl

    @Mock
    private lateinit var remoteDataSourceCharacters: DataSource<CharactersResponseDTO, CharacterDataDTO>

    @Mock
    private lateinit var remoteDataSourceEpisode: DataSource<EpisodesResultDTO, EpisodeDTO>

    @Mock
    private lateinit var remoteDataSourceLocations: DataSource<LocationsResultDTO, LocationDTO>

    @Mock
    private lateinit var localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity, LocationEntity>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = RepositoryImpl(
            remoteDataSourceCharacters,
            remoteDataSourceEpisode,
            remoteDataSourceLocations,
            localDataSource
        )
    }

    @Test
    fun getDataById_remoteRepos() {
        runBlocking {
            val characterDataDTO = CharacterDataDTO(
                "test", listOf(), "male", 1, "url", Location("", ""),
                "name", Origin("", ""), "human", "alive", "type", "url"
            )
            val id = 1

            Mockito.`when`(remoteDataSourceCharacters.getDataById(id)).thenReturn(characterDataDTO)
            repository.getCharacterDetails(id)

            Mockito.verify(remoteDataSourceCharacters, Mockito.times(1)).getDataById(id)
        }
    }

    @Test
    fun getEpisodeById_remoteRepos() {
        runBlocking {
            val episodeDTO = EpisodeDTO(
                1, "test", "test", listOf(), "test",
                "test", "test"
            )
            val id = 1

            Mockito.`when`(remoteDataSourceEpisode.getDataById(id)).thenReturn(episodeDTO)
            repository.getEpisodeById(id)

            Mockito.verify(remoteDataSourceEpisode, Mockito.times(1)).getDataById(id)
        }
    }

    @Test
    fun getLocationById_remoteRepos() {
        runBlocking {
            val location = LocationDTO(
                1, "test", "test", "test", listOf(),
                "test", "test"
            )
            val id = 1

            Mockito.`when`(remoteDataSourceLocations.getDataById(id)).thenReturn(location)
            repository.getLocationById(id)

            Mockito.verify(remoteDataSourceLocations, Mockito.times(1)).getDataById(id)
        }
    }


}