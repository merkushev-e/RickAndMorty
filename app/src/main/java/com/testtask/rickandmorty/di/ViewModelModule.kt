package com.testtask.rickandmorty.di

import androidx.lifecycle.ViewModel
import com.testtask.rickandmorty.data.repositories.RepositoryImpl
import com.testtask.rickandmorty.presentation.character.viewModel.CharacterDetailsViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.presentation.episodes.EpisodesViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
class ViewModelModule {
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    @Provides
    fun provideCharactersViewModel(repository: RepositoryImpl): ViewModel {
        return CharactersViewModel(repository)
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
}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)