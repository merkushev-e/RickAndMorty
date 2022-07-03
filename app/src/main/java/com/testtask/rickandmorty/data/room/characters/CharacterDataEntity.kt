package com.testtask.rickandmorty.data.room.characters

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class CharacterDataEntity(
    val created: String,
    @field:TypeConverters(EpisodeConverter::class)
    val episode: List<String>,
    val gender: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val locationName: String,
    val locationUrl: String,
    val name: String,
    val origin: String,
    val species: String,
    val status: String ,
    val type: String,
    val url: String
)