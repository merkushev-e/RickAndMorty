package com.testtask.rickandmorty.domain.model


data class EpisodeData(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>
)