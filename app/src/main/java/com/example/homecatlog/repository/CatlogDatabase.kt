package com.example.homecatlog.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.homecatlog.entity.Catlog

@Database(entities = [Catlog::class], version = 1, exportSchema = false)
abstract class CatlogDatabase : RoomDatabase() {
    abstract fun catlogDao(): CatlogDao

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
}
