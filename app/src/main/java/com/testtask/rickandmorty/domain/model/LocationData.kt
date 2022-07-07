package com.testtask.rickandmorty.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LocationData(
    val id: Int = 1,
    val name: String = "",
    val type: String = "",
    val dimension: String = "",
    val residents: List<String> = listOf(),
    val url: String = "",
    val created: String = ""
): Parcelable
