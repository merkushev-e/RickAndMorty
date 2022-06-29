package com.testtask.rickandmorty.di

import androidx.paging.PagingData
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.data.retrofit.RMApi
import com.testtask.rickandmorty.data.retrofit.RMApiFactory
import com.testtask.rickandmorty.data.retrofit.RemoteDataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.CharactersData
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    fun provideRepository (dataSource: RemoteDataSource): Repository {
        return RepositoryImpl(dataSource)
    }


    @Provides
    @Singleton
    fun provideRemoteDataSource() :  DataSource<CharactersResponseDTO, CharacterDataDTO, EpisodesResultDTO, EpisodeDTO> = RemoteDataSource(RMApiFactory.create())
}