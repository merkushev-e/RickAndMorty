package com.testtask.rickandmorty.data.room.location

import androidx.room.TypeConverter
import java.util.stream.Collectors

class ResidentsConverter {
    @TypeConverter
    fun fromResidents(character: List<String>): String{
        return character.stream().collect(Collectors.joining(","))
    }
    @TypeConverter
    fun toResidents(string: String):  List<String>{
        return string.split(",")
    }
}