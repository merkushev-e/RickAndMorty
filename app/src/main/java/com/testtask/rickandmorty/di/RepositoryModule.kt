package com.testtask.rickandmorty.di

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.RemoteDataSourceEpisodes
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.data.retrofit.RMApiFactory
import com.testtask.rickandmorty.data.retrofit.RemoteDataCharacters
import com.testtask.rickandmorty.data.retrofit.RemoteDataSourceLocations
import com.testtask.rickandmorty.data.retrofit.model.*
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    fun provideRepository(
        characters: RemoteDataCharacters,
        episodes: RemoteDataSourceEpisodes,
        locations: RemoteDataSourceLocations
    ): Repository {
        return RepositoryImpl(characters, episodes,locations)
    }


    @Provides
    @Singleton
    fun provideRemoteDataSourceCharacters(): DataSource<CharactersResponseDTO, CharacterDataDTO> =
        RemoteDataCharacters(RMApiFactory.create())

    @Provides
    @Singleton
    fun provideRemoteDataSourceEpisodes(): DataSource<EpisodesResultDTO, EpisodeDTO> =
        RemoteDataSourceEpisodes(RMApiFactory.create())


    @Provides
    @Singleton
    fun provideRemoteDataSourceLocations(): DataSource<LocationsResultDTO, LocationDTO> =
        RemoteDataSourceLocations(RMApiFactory.create())


}
