package com.testtask.rickandmorty.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.utils.toLocationData

class PageSourceLocalLocations (private val localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity, LocationEntity>) :
    PagingSource<Int, LocationData>() {
    override fun getRefreshKey(state: PagingState<Int, LocationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationData> {

        try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val offset = page * pageSize

            val response =
                localDataSource.getLocationDataByPages(limit = pageSize, offset = offset).map {
                    it.toLocationData()
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