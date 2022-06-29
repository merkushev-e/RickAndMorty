package com.testtask.rickandmorty.domain

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodeDTO
import com.testtask.rickandmorty.data.retrofit.model.EpisodesResultDTO
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.utils.toCharactersData


class PageSource(
    private val remoteDataSource: DataSource<CharactersResponseDTO, CharacterDataDTO, EpisodesResultDTO, EpisodeDTO>
) : PagingSource<Int, CharactersData>() {
    override fun getRefreshKey(state: PagingState<Int, CharactersData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersData> {


        try {
            val page: Int = params.key ?: 1

            val response = remoteDataSource.getCharactersByPages(page)
            var nextKey: Int? = null
            if (response.info.next != null) {
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextKey = nextPageQuery?.toInt()
            }

            val results = response.results.map { it.toCharactersData() }
            val prevKey = if (page == 1) null else page - 1

            return LoadResult.Page(results, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}