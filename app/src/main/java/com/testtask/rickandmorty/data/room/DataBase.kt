package com.testtask.rickandmorty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity

@Database(entities = arrayOf(CharacterDataEntity::class), version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract fun dbDao(): DbDao
}