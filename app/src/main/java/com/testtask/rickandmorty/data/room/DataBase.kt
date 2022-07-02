package com.testtask.rickandmorty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity

@Database(entities = arrayOf(CharacterDataEntity::class, EpisodeEntity::class), version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract fun dbDao(): DbDao
}