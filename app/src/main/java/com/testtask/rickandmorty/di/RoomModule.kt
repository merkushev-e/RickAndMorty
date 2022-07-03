package com.testtask.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.testtask.rickandmorty.data.room.DataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    companion object {
        private const val DB_NAME = "Characters.db"
    }
    @Provides
    @Singleton
    fun provideRoom(app: Context): DataBase {
        return Room.databaseBuilder(
            app,
            DataBase::class.java,
            DB_NAME
        ).build()
    }
}