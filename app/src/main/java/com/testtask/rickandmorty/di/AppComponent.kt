package com.testtask.rickandmorty.di

import android.content.Context
import com.testtask.rickandmorty.data.retrofit.RMApi
import com.testtask.rickandmorty.presentation.MainActivity
import com.testtask.rickandmorty.presentation.character.CharacterDetailsFragment
import com.testtask.rickandmorty.presentation.character.CharactersFragment
import dagger.BindsInstance
import dagger.BindsOptionalOf
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)

interface AppComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setContext(context: Context): Builder
        fun build(): AppComponent
    }


    fun inject(mainActivity: MainActivity)
    fun inject(charactersFragment: CharactersFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)
}