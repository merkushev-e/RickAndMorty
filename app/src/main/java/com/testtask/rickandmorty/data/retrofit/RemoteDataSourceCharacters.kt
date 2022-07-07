package com.testtask.rickandmorty.data.retrofit

import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.EpisodesResultData
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState

class RemoteDataCharacters(private val api: RMApi) :
    DataSource<CharactersResponseDTO, CharacterDataDTO> {

    override suspend fun getDataByPages(
        page: Int
    ): CharactersResponseDTO {
        return api.getAllCharacters(page)
    }

    override suspend fun getDataById(id: Int): CharacterDataDTO {
        return api.getCharacter(id)
    }

    override suspend fun getDataByPagesWithFilters(
        page: Int,
        status: String,
        gender: String,
        name: String
    ): CharactersResponseDTO {
        return api.getAllCharactersWithFilters(status,gender,name,page)
    }

}