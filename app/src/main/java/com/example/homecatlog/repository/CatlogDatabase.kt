package com.example.homecatlog.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homecatlog.entity.Catlog

@Database(entities = [Catlog::class], version = 1, exportSchema = false)
abstract class CatlogDatabase : RoomDatabase() {
    abstract fun catlogDao(): CatlogDao
}
