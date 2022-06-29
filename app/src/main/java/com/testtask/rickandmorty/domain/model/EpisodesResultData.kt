package com.testtask.rickandmorty.domain.model

import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.Info

data class EpisodesResultData(
    val info: Info,
    val results: List<EpisodeDTO>
)
