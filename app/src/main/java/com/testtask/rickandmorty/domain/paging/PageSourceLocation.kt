package com.testtask.rickandmorty.domain.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.testtask.rickandmorty.data.DataSource
import com.testtask.rickandmorty.data.retrofit.model.LocationDTO
import com.testtask.rickandmorty.data.retrofit.model.LocationsResultDTO
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.utils.toLocationData

class PageSourceLocation(
    private val remoteDataSource: DataSource<LocationsResultDTO, LocationDTO>
) : PagingSource<Int, LocationData>() {

    override fun getRefreshKey(state: PagingState<Int, LocationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, LocationData> {


        try {
            val page: Int = params.key ?: 1

            val response = remoteDataSource.getDataByPages(page)
            var nextKey: Int? = null
            if (response.info.next != null) {
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextKey = nextPageQuery?.toInt()
            }

            val results = response.results.map { it.toLocationData() }
            val prevKey = if (page == 1) null else page - 1

            return PagingSource.LoadResult.Page(results, prevKey, nextKey)
        } catch (e: Exception) {
            return PagingSource.LoadResult.Error(e)
        }
    }
}
