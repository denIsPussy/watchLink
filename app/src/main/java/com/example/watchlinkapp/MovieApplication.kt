package com.example.watchlinkapp

import android.app.Application
import com.example.watchlinkapp.Database.AppContainer
import com.example.watchlinkapp.Database.AppDataContainer

class MovieApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}