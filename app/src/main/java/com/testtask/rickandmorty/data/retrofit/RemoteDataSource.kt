package com.testtask.rickandmorty.data.retrofit

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.EpisodesResultData

class RemoteDataSource(private val api: RMApi) :
    DataSource<CharactersResponseDTO, CharacterDataDTO, EpisodesResultDTO, EpisodeDTO> {

    override suspend fun getCharactersByPages(page: Int): CharactersResponseDTO {
        return api.getAllCharacters(page)
    }

    override suspend fun getCharacterDetailsById(id: Int): CharacterDataDTO {
        return api.getCharacter(id)
    }

    override suspend fun getEpisodesByPages(page: Int): EpisodesResultDTO {
        return api.getAllEpisode(page)
    }

    override suspend fun getEpisodesDetailsById(id: Int): EpisodeDTO {
        return api.getEpisodeById(id)
    }


}