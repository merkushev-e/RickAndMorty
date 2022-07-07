package com.testtask.rickandmorty.utils


import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.LocationDTO
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.LocationData


internal fun EpisodeDTO.toEpisodeData(): EpisodeData {
    return EpisodeData(
        id = id,
        name = name,
        air_date = air_date,
        episode = episode,
        characters = characters
    )
}

internal fun CharacterDataDTO.toCharactersData(): CharactersData {
    return CharactersData(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = CharactersData.Location(location.name, location.url),
        name = name,
        origin = CharactersData.Origin(origin.name, origin.url),
        species = species,
        status = status,
        type = type,
        url = url
    )
}

internal fun CharactersData.toCharacterDataEntity(): CharacterDataEntity {
    return CharacterDataEntity(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        locationName = location.name,
        locationUrl = location.url,
        name = name,
        origin = origin.name,
        species = species,
        status = status,
        type = type,
        url = url
    )
}

internal fun CharacterDataEntity.toCharactersData(): CharactersData {
    return CharactersData(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = CharactersData.Location(locationName,locationUrl),
        name = name,
        origin = CharactersData.Origin(origin,""),
        species = species,
        status = status,
        type = type,
        url = url
    )
}

internal fun LocationDTO.toLocationData(): LocationData {
    return LocationData(
        id = id,
        name = name,
        type = type,
        residents = residents,
        dimension = dimension,
        url = url,
        created = created
    )
}

internal fun EpisodeEntity.toEpisodeData(): EpisodeData{
    return EpisodeData(
        id = id,
        name = name,
        air_date = air_date,
        episode = episode,
        characters = characters,

    )
}

internal fun EpisodeData.toEpisodeEntity(): EpisodeEntity{
    return EpisodeEntity(
        id = id,
        name = name,
        air_date = air_date,
        episode = episode,
        characters = characters
    )
}

internal fun LocationEntity.toLocationData(): LocationData {
    return LocationData(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents,
        url = url,
        created = created
    )
}

internal fun LocationData.toLocationEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents,
        url = url,
        created = created
    )
}





