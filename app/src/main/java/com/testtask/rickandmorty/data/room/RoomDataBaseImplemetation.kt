package com.testtask.rickandmorty.data.room

import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import javax.inject.Inject


class RoomDataBaseImplementation @Inject constructor(private val dbDao: DbDao) :
    LocalDataSource<CharacterDataEntity, EpisodeEntity, LocationEntity> {

    override suspend fun getDataByPages(page: Int): CharacterDataEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getDataById(id: Int): EpisodeEntity {
        TODO("Not yet implemented")
    }

    override suspend fun saveToDb(dataList: List<CharacterDataEntity>) {
        dbDao.insertAll(dataList)
    }

    override suspend fun getDataByPages(
        limit: Int, offset: Int,
        status: String,
        gender: String,
        searchBy: String,
    ): List<CharacterDataEntity> {
        return dbDao.getData(limit,offset,status,gender,searchBy)
    }

    override suspend fun saveEpisodeToDb(dataList: List<EpisodeEntity>) {
        return dbDao.insertAllEpisode(dataList)
    }

    override suspend fun getEpisodeDataByPages(limit: Int, offset: Int): List<EpisodeEntity> {
        return dbDao.getEpisodeData(limit, offset)
    }

    override suspend fun saveLocationToDb(dataList: List<LocationEntity>) {
        dbDao.insertAllLocations(dataList)
    }

    override suspend fun getLocationDataByPages(limit: Int, offset: Int): List<LocationEntity> {
        return dbDao.getLocationData(limit, offset)
    }


    override suspend fun getDataByPagesWithFilters(
        page: Int,
        status: String,
        gender: String,
        name: String
    ): CharacterDataEntity {
        TODO("Not yet implemented")
    }
}