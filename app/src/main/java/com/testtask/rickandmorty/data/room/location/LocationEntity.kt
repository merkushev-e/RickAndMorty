package com.testtask.rickandmorty.data.room.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity
data class LocationEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @field:TypeConverters(ResidentsConverter::class)
    val residents: List<String>,
    val url: String,
    val created: String
)