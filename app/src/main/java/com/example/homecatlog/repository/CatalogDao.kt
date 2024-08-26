package com.example.homecatlog.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homecatlog.entity.Catalog
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {

    @Query("SELECT * FROM Catalog")
    fun getAllCatlogs(): Flow<List<Catalog>>

    @Query("SELECT * FROM Catalog WHERE category = :category")
    fun getCatlogByCategory(category: String): Flow<Catalog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCatlog(catalog: Catalog)

    @Query("DELETE FROM Catalog WHERE category = :category")
    suspend fun deleteCatlog(category: String)

    @Update
    suspend fun updateCatlog(catalog: Catalog)
}