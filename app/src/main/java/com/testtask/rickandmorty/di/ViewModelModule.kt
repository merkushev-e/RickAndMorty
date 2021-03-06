package com.testtask.rickandmorty.di

import androidx.lifecycle.ViewModel
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.presentation.character.viewModel.CharacterDetailsViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.presentation.episodes.viewmodel.EpisodeDetailViewModel
import com.testtask.rickandmorty.presentation.episodes.viewmodel.EpisodesViewModel
import com.testtask.rickandmorty.presentation.locations.viewmodels.LocationsDetailViewModel
import com.testtask.rickandmorty.presentation.locations.viewmodels.LocationsViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

@Module
class ViewModelModule {
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    @Provides
    fun provideCharactersViewModel(repository: RepositoryImpl): ViewModel {
        return CharactersViewModel(repository, Dispatchers.IO)
    }

    @IntoMap
    @ViewModelKey(CharacterDetailsViewModel::class)
    @Provides
    fun provideCharactersDetailsViewModel(repository: RepositoryImpl): ViewModel {
        return CharacterDetailsViewModel(repository)
    }

    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    @Provides
    fun provideEpisodesViewModel(repository: RepositoryImpl): ViewModel {
        return EpisodesViewModel(repository)
    }

    @IntoMap
    @ViewModelKey(EpisodeDetailViewModel::class)
    @Provides
    fun provideEpisodeDetailViewModel(repository: RepositoryImpl): ViewModel {
        return EpisodeDetailViewModel(repository)
    }

    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    @Provides
    fun provideLocationsViewModel(repository: RepositoryImpl): ViewModel {
        return LocationsViewModel(repository)
    }

    @IntoMap
    @ViewModelKey(LocationsDetailViewModel::class)
    @Provides
    fun provideLocationsDetailViewModel(repository: RepositoryImpl): ViewModel {
        return LocationsDetailViewModel(repository)
    }
}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)