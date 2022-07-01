package com.testtask.rickandmorty.domain.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.CharacterDataDTO
import com.testtask.rickandmorty.data.retrofit.model.CharactersResponseDTO
import com.testtask.rickandmorty.data.room.characters.CharacterDataEntity
import com.testtask.rickandmorty.data.room.LocalDataSource
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.utils.toCharacterDataEntity
import com.testtask.rickandmorty.utils.toCharactersData


class PageSource(
    private val remoteDataSource: DataSource<CharactersResponseDTO, CharacterDataDTO>,
    private val localDataSource: LocalDataSource<CharacterDataEntity, CharacterDataEntity>
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

            val response = remoteDataSource.getDataByPages(page)
            var nextKey: Int? = null
            if (response.info.next != null) {
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextKey = nextPageQuery?.toInt()
            }

            val results = response.results.map { it.toCharactersData() }
            localDataSource.saveToDb(results.map {
                it.toCharacterDataEntity()
            })
            val prevKey = if (page == 1) null else page - 1

            return LoadResult.Page(results, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}