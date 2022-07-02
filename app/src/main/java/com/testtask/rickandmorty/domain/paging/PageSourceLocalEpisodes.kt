package com.testtask.rickandmorty.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.utils.toEpisodeData

class PageSourceLocalEpisodes (private val localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity>) :
    PagingSource<Int, EpisodeData>() {
    override fun getRefreshKey(state: PagingState<Int, EpisodeData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeData> {

        try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val offset = page * pageSize

            val response =
                localDataSource.getEpisodeDataByPages(limit = pageSize, offset = offset).map {
                    it.toEpisodeData()
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


