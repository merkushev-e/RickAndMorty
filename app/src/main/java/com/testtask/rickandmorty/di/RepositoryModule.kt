package com.testtask.rickandmorty.di

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.domain.Repository
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.data.retrofit.*
import com.testtask.rickandmorty.data.retrofit.model.*
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.DataBase
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.RoomDataBaseImplementation
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        characters: RemoteDataCharacters,
        episodes: RemoteDataSourceEpisodes,
        locations: RemoteDataSourceLocations,
        localDataSource: RoomDataBaseImplementation
    ): Repository {
        return RepositoryImpl(characters, episodes,locations, localDataSource)
    }


    @Provides
    @Singleton
    fun provideRemoteDataSourceCharacters(rmApi: RMApi): DataSource<CharactersResponseDTO, CharacterDataDTO> =
        RemoteDataCharacters(rmApi)

    @Provides
    @Singleton
    fun provideRemoteDataSourceEpisodes(rmApi: RMApi): DataSource<EpisodesResultDTO, EpisodeDTO> =
        RemoteDataSourceEpisodes(rmApi)


    @Provides
    @Singleton
    fun provideRemoteDataSourceLocations(rmApi: RMApi): DataSource<LocationsResultDTO, LocationDTO> =
        RemoteDataSourceLocations(rmApi)


    @Provides
    @Singleton
    fun provideLocalDataSource(db: DataBase): LocalDataSource<CharacterDataEntity, EpisodeEntity> =
        RoomDataBaseImplementation(db.dbDao())

}
