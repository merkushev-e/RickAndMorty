package com.testtask.rickandmorty.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.utils.toCharactersData

class PageSourceLocalCharacters(private val localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity, LocationEntity>) :
    PagingSource<Int, CharactersData>() {
    override fun getRefreshKey(state: PagingState<Int, CharactersData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersData> {

        try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val offset = page * pageSize

            val response = localDataSource.getDataByPages(limit = pageSize, offset = offset).map {
                it.toCharactersData()
            }
            return LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.size == params.loadSize) page + (params.loadSize / pageSize) else null
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


}