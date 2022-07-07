package com.testtask.rickandmorty.data.room.episodes

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
class EpisodeEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    @field:TypeConverters(CharacterConverter::class)
    val characters: List<String>,
)
