package com.testtask.rickandmorty.data

interface DataSource<T> {
    suspend fun getData(page: Int): T
}