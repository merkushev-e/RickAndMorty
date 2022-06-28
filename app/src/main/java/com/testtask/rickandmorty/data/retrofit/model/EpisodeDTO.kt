package com.testtask.rickandmorty.data.retrofit.model

data class EpisodeDTO(
    val id: Int,
    val name: String,
    val air_date: String,
    val characters: List<String>,
    val url: String,
    val episode: String,
    val created: String
)
