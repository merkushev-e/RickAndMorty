package com.testtask.rickandmorty.data.room

import androidx.room.*
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity


@Dao
interface DbDao {
    @Query("SELECT * FROM CharacterDataEntity")
    suspend fun all(): List<CharacterDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<CharacterDataEntity>)

    @Query("SELECT * FROM CharacterDataEntity " + "LIMIT :limit OFFSET :offset")
    suspend fun getData(limit: Int, offset: Int): List<CharacterDataEntity>
}