package com.testtask.rickandmorty.data.room

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity

interface LocalDataSource<T,U> : DataSource<T,U> {
    suspend fun saveToDb(dataList: List<T>)
    suspend fun getDataByPages(limit: Int, offset: Int): List<T>
    suspend fun saveEpisodeToDb(dataList: List<U>)
    suspend fun getEpisodeDataByPages(limit: Int, offset: Int): List<U>

}