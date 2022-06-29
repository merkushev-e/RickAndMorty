package com.testtask.rickandmorty.domain.model

import android.os.Parcelable
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.Info
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodesResultData(
    val results: List<EpisodeData>
) : Parcelable
