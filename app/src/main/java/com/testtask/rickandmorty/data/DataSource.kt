package com.testtask.rickandmorty.data



interface DataSource<T, U> {
    suspend fun getDataByPages(
        page: Int
    ): T

    suspend fun getDataByPagesWithFilters(
        page: Int, status: String,
        gender: String,
        name: String
    ): T

    suspend fun getDataById(id: Int): U

}