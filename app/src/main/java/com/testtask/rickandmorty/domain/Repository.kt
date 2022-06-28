package com.testtask.rickandmorty.domain

import com.testtask.rickandmorty.domain.model.CharactersData


interface Repository<T> {
     fun getData() : T
     fun saveData(appState: AppState)
     suspend fun getCharacterDetails(id: Int): CharactersData



}