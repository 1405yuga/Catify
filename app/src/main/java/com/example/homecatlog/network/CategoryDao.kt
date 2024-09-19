package com.example.homecatlog.network

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homecatlog.entity.Catalog
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Catalog")
    fun getAllCatalogs(): Flow<List<Catalog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCatalog(catalog: Catalog)
}