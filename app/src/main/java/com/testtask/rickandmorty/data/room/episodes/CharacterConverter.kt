package com.testtask.rickandmorty.data.room.episodes

import androidx.room.TypeConverter
import java.util.stream.Collectors

class CharacterConverter {
    @TypeConverter
    fun fromCharacter(character: List<String>): String{
        return character.stream().collect(Collectors.joining(","))
    }
    @TypeConverter
    fun toCharacter(string: String):  List<String>{
        return string.split(",")
    }
}