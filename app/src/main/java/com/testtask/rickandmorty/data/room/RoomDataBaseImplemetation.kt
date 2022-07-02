package com.testtask.rickandmorty.data.room

import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import javax.inject.Inject


class RoomDataBaseImplementation @Inject constructor(private val dbDao: DbDao): LocalDataSource<CharacterDataEntity, EpisodeEntity> {
    override suspend fun getDataByPages(page: Int): CharacterDataEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getDataById(id: Int): EpisodeEntity {
        TODO("Not yet implemented")
    }

    override suspend fun saveToDb(dataList: List<CharacterDataEntity>) {
        dbDao.insertAll(dataList)
    }

    override suspend fun getDataByPages(limit: Int, offset: Int): List<CharacterDataEntity> {
       return dbDao.getData(limit,offset)
    }

    override suspend fun saveEpisodeToDb(dataList: List<EpisodeEntity>) {
        return dbDao.insertAllEpisode(dataList)
    }
    override suspend fun getEpisodeDataByPages(limit: Int, offset: Int): List<EpisodeEntity> {
        return dbDao.getEpisodeData(limit,offset)
    }
}