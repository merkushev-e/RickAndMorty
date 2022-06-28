package com.testtask.rickandmorty.utils


import androidx.fragment.app.Fragment
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.di.AppComponent
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData


internal fun EpisodeDTO.toEpisodeData(): EpisodeData{
    return EpisodeData(
        id = id,
        name = name,
        air_date = air_date,
        episode = episode,
        characters = characters
    )
}

internal fun CharacterDataDTO.toCharactersData():  CharactersData{
    return CharactersData(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = CharactersData.Location(location.name,location.url),
        name = name,
        origin = CharactersData.Origin(origin.name,origin.url),
        species = species,
        status = status,
        type = type,
        url = url
    )
}

