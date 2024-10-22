package com.example.catify.network

import android.app.Application

class BaseApplication : Application() {
    val database: CatalogDatabase by lazy {
        CatalogDatabase.getCatalogDatabase(this)
    }
}