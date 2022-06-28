package com.testtask.rickandmorty.domain

import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData


interface Repository<T> {
     fun getData() : T
     fun saveData(appState: AppState)
     suspend fun getCharacterDetails(id: Int): CharactersData
     suspend fun getEpisodeById(id: Int): EpisodeData



}