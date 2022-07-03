package com.testtask.rickandmorty.data.room.characters

import androidx.room.TypeConverter
import java.util.stream.Collectors

class EpisodeConverter {

    @TypeConverter
    fun fromEpisode(episode: List<String>): String{
        return episode.stream().collect(Collectors.joining(","))
    }
    @TypeConverter
    fun toEpisode(string: String):  List<String>{
        return string.split(",")
    }
}

