package com.testtask.rickandmorty.data.room

import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import javax.inject.Inject


class RoomDataBaseImplementation @Inject constructor(private val dbDao: DbDao): LocalDataSource<CharacterDataEntity, CharacterDataEntity> {
    override suspend fun getDataByPages(page: Int): CharacterDataEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getDataById(id: Int): CharacterDataEntity {
        TODO("Not yet implemented")
    }

    override suspend fun saveToDb(dataList: List<CharacterDataEntity>) {
        dbDao.insertAll(dataList)
    }

    override suspend fun getDataByPages(limit: Int, offset: Int): List<CharacterDataEntity> {
       return dbDao.getData(limit,offset)
    }
}