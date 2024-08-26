package com.example.homecatlog

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.homecatlog.repository.CatlogDatabase

//used to maintain global application state
class BaseApplication : Application() {

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: CatlogDatabase? = null

        fun getCatlogDatabase(context: Context): CatlogDatabase {
            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, CatlogDatabase::class.java, "catlog_datbase")
                        .fallbackToDestructiveMigration()
                        .build()
                DATABASE_INSTANCE = instance
                return instance
            }
        }
    }

    val database: CatlogDatabase by lazy {
        getCatlogDatabase(this)
    }
}