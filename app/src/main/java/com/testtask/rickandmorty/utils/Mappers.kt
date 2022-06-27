package com.testtask.rickandmorty.utils


import androidx.fragment.app.Fragment
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.di.AppComponent
import com.testtask.rickandmorty.domain.model.CharactersData


fun Fragment.getAppComponent(): AppComponent =
    (requireContext() as App).component

internal fun CharacterDataDTO.toCharactersData():  CharactersData{
    return CharactersData(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = location,
        name = name,
        origin = origin,
        species = species,
        status = status,
        type = type,
        url = url
    )
}