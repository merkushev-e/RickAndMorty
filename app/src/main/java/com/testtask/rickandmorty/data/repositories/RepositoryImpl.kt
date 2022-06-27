package com.testtask.rickandmorty.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.PageSource
import com.testtask.rickandmorty.data.retrofit.RMApi
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.domain.model.CharactersData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
}