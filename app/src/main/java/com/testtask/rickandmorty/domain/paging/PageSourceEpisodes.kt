package com.testtask.rickandmorty.domain.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.episodes.EpisodeEntity
import com.testtask.rickandmorty.data.room.location.LocationEntity
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.utils.toEpisodeData
import com.testtask.rickandmorty.utils.toEpisodeEntity

class PageSourceEpisodes(
    private val remoteDataSource: DataSource<EpisodesResultDTO, EpisodeDTO>,
    private val localDataSource: LocalDataSource<CharacterDataEntity, EpisodeEntity,LocationEntity>
) : PagingSource<Int, EpisodeData>() {

    override fun getRefreshKey(state: PagingState<Int, EpisodeData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, EpisodeData> {


        try {
            val page: Int = params.key ?: 1

            val response = remoteDataSource.getDataByPages(page)
            var nextKey: Int? = null
            if (response.info.next != null) {
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextKey = nextPageQuery?.toInt()
            }

            val results = response.results.map { it.toEpisodeData() }
            localDataSource.saveEpisodeToDb(results.map {
                it.toEpisodeEntity()
            })

            val prevKey = if (page == 1) null else page - 1

            return PagingSource.LoadResult.Page(results, prevKey, nextKey)
        } catch (e: Exception) {
            return PagingSource.LoadResult.Error(e)
        }
    }
}

