package com.testtask.rickandmorty.data.room

import androidx.room.*
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity


@Dao
interface DbDao {
    @Query("SELECT * FROM CharacterDataEntity")
    suspend fun all(): List<CharacterDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<CharacterDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisode(entities: List<EpisodeEntity>)

    @Query("SELECT * FROM CharacterDataEntity " + "LIMIT :limit OFFSET :offset")
    suspend fun getData(limit: Int, offset: Int): List<CharacterDataEntity>

    @Query("SELECT * FROM EpisodeEntity " + "LIMIT :limit OFFSET :offset")
    suspend fun getEpisodeData(limit: Int, offset: Int): List<EpisodeEntity>
}