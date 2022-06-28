package com.testtask.rickandmorty.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.PageSource
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.utils.toCharactersData
import com.testtask.rickandmorty.utils.toEpisodeData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val remoteDataSource: DataSource<CharactersResponseDTO>
) : Repository<Flow<PagingData<CharactersData>>> {
    override fun getData(): Flow<PagingData<CharactersData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PageSource(remoteDataSource) }
        ).flow


    }


    override fun saveData(appState: AppState) {
        TODO("Not yet implemented")

    }

    override suspend fun getCharacterDetails(id: Int): CharactersData {
       return remoteDataSource.getCharacterDetails(id).toCharactersData()
    }

    override suspend fun getEpisodeById(id: Int): EpisodeData {
        return remoteDataSource.getEpisodeById(id).toEpisodeData()
    }


}