package com.testtask.rickandmorty.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodeData(
    val id: Int = 1,
    val name: String = "",
    val air_date: String = "",
    val episode: String = "",
    val characters: List<String> = listOf()
): Parcelable