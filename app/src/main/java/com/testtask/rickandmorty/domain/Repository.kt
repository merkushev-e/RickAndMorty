package com.testtask.rickandmorty.domain


interface Repository<T> {
     fun getData() : T
     fun saveData(appState: AppState)



}