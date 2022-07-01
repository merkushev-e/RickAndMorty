package com.testtask.rickandmorty.data.room

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity

interface LocalDataSource<T,U> : DataSource<T,U> {
    suspend fun saveToDb(dataList: List<CharacterDataEntity>)
    suspend fun getDataByPages(limit: Int, offset: Int): List<CharacterDataEntity>

}