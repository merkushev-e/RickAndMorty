package com.testtask.rickandmorty.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.domain.PageSource
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.PageSourceEpisodes
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.EpisodesResultData
import com.testtask.rickandmorty.utils.toCharactersData
import com.testtask.rickandmorty.utils.toEpisodeData
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.CharacterData
import java.security.PrivateKey
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val remoteDataSource: DataSource<CharactersResponseDTO, CharacterDataDTO, EpisodesResultDTO, EpisodeDTO>) : Repository {

    override fun getCharactersByPage(): Flow<PagingData<CharactersData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSource(remoteDataSource) }
        ).flow
    }


    override fun saveData(appState: AppState) {
        TODO("Not yet implemented")

    }

    override suspend fun getCharacterDetails(id: Int): CharactersData {
       return remoteDataSource.getCharacterDetailsById(id).toCharactersData()
    }

    override suspend fun getEpisodeById(id: Int): EpisodeData {
        return remoteDataSource.getEpisodesDetailsById(id).toEpisodeData()
    }

    override fun getAllEpisode(): Flow<PagingData<EpisodeData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSourceEpisodes(remoteDataSource) }
        ).flow
    }


}