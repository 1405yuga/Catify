package com.example.homecatlog.network

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.entity.HomeItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Catalog")
    fun getAllCatalogs(): Flow<List<Catalog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCatalog(catalog: Catalog): Long

    @Delete
    suspend fun deleteCatalog(catalog: Catalog): Int

    @Query("SELECT * FROM Catalog WHERE category = :category")
    suspend fun getCatalogByCategory(category: String): Catalog?

    @Update
    suspend fun updateCatalog(catalog: Catalog)
}