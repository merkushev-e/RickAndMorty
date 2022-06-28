package com.testtask.rickandmorty.domain.model

import android.os.Parcelable
import com.testtask.rickandmorty.data.retrofit.model.Location
import com.testtask.rickandmorty.data.retrofit.model.Origin
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersData(
    val created: String = "",
    val episode: List<String> = emptyList(),
    val gender: String = "",
    val id: Int = 1,
    val image: String = "",
    val location: Location,
    val name: String = "",
    val origin: Origin,
    val species: String = "",
    val status: String = "",
    val type: String = "",
    val url: String = ""
) : Parcelable{

    @Parcelize
    data class Location(
        val name: String = "",
        val url: String = ""
    ) : Parcelable

    @Parcelize
    data class Origin(
        val name: String = "",
        val url: String = ""
    ): Parcelable
}


