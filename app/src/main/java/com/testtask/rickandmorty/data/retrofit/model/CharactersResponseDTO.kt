package com.testtask.rickandmorty.data.retrofit.model



data class CharactersResponseDTO(
    val info: Info,
    val results: List<CharacterDataDTO>
)

data class Info(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: Any
)
