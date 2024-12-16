package com.example.catify.network.catalog_db

import android.app.Application

class BaseApplication : Application() {
    val database: CatalogDatabase by lazy {
        CatalogDatabase.getCatalogDatabase(this)
    }
}