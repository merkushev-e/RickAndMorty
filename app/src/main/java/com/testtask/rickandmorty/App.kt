package com.testtask.rickandmorty

import android.app.Application
import com.testtask.rickandmorty.di.AppComponent
import com.testtask.rickandmorty.di.DaggerAppComponent

class App : Application() {
    lateinit var component: AppComponent

    companion object {
        lateinit var instance: App
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.builder()
            .setContext(this)
            .build()

    }
}