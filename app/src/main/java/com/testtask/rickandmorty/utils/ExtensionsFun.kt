package com.testtask.rickandmorty.utils

import androidx.fragment.app.Fragment
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.di.AppComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan


fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}

fun Fragment.getAppComponent(): AppComponent =
    (requireContext() as App).component