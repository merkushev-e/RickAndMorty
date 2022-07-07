package com.testtask.rickandmorty

import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.Location
import com.testtask.rickandmorty.data.retrofit.model.Origin
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.utils.toCharacterDataEntity
import com.testtask.rickandmorty.utils.toCharactersData
import com.testtask.rickandmorty.utils.toEpisodeData
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot.not
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsMappersTests {

    @Test
    fun episodeDtoToEpisodeData_shouldBeEqual() {
        val episodeDTO = EpisodeDTO(1, "test", "test", listOf(), "test",
            "test", "test")
        val resultActual = episodeDTO.toEpisodeData()
        val resultExpected: EpisodeData = EpisodeData(1, "test", "test",
            "test", listOf())
        Assert.assertEquals(resultExpected, resultActual)

    }

    @Test
    fun episodeDtoToEpisodeData_shouldNotBeEqual() {
        val episodeDTO = EpisodeDTO(1, "test", "test", listOf(), "test",
            "test", "test")
        val resultActual = episodeDTO.toEpisodeData()
        val resultExpected: EpisodeData = EpisodeData(0, "tes2", "test2", "test", listOf())
        assertThat(resultExpected, not(resultActual))

    }

    @Test
    fun characterDataDTOtoCharactersData_shouldBeEqual() {
        val characterDataDTO = CharacterDataDTO(
            "test", listOf(), "male", 1, "url", Location("", ""),
            "name", Origin("", ""), "human", "alive", "type", "url"
        )
        val expectedResult: CharactersData = CharactersData("test", listOf(), "male",
            1, "url", CharactersData.Location("", ""), "name",
            CharactersData.Origin("", ""), "human", "alive", "type", "url")
        val actualResult = characterDataDTO.toCharactersData()

        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun charactersDataToCharacterDataEntity_ShouldBeEqual(){
        val characterData: CharactersData = CharactersData("test", listOf(), "male",
            1, "url", CharactersData.Location("", ""), "name",
            CharactersData.Origin("", ""), "human", "alive", "type", "url")

        val expectedResult = CharacterDataEntity("test", listOf(), "male",
            1, "url", "", "",
            "name", "","human", "alive", "type", "url")

        val actualResult = characterData.toCharacterDataEntity()

        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun characterDataEntityToCharactersData_ShouldNotBeEqual(){
        val characterDataEntity = CharacterDataEntity("test2", listOf(), "male",
            2, "url", "", "",
            "name", "","human", "alive", "type", "url")

        val expectedResult =  CharactersData("test", listOf(), "male",
            1, "url", CharactersData.Location("", ""), "name",
            CharactersData.Origin("", ""), "human", "alive", "type", "url")

        val actualResult = characterDataEntity.toCharactersData()
        assertThat(actualResult, not(expectedResult))
        
    }



}