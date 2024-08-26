package com.example.homecatlog.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homecatlog.entity.Catalog

@Database(entities = [Catalog::class], version = 1, exportSchema = false)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun catlogDao(): CatalogDao
}
