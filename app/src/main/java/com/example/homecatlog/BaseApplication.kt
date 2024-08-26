package com.example.homecatlog

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.homecatlog.repository.CatalogDatabase

//used to maintain global application state
class BaseApplication : Application() {

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: CatalogDatabase? = null

        fun getCatlogDatabase(context: Context): CatalogDatabase {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, CatalogDatabase::class.java, "catlog_datbase")
                        .fallbackToDestructiveMigration()
                        .build()
                DATABASE_INSTANCE = instance
                return instance
            }
        }
    }

    val database: CatalogDatabase by lazy {
        getCatlogDatabase(this)
    }
}