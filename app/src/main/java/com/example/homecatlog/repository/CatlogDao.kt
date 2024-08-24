package com.example.homecatlog.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homecatlog.entity.Catlog
import kotlinx.coroutines.flow.Flow

@Dao
interface CatlogDao {

    @Query("SELECT * FROM Catlog")
    fun getAllCatlogs(): Flow<List<Catlog>>

    @Query("SELECT * FROM Catlog WHERE category = :category")
    fun getCatlogByCategory(category: String): Flow<Catlog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCatlog(catlog: Catlog)

    @Query("DELETE FROM Catlog WHERE category = :category")
    suspend fun deleteCatlog(category: String)

    @Update
    suspend fun updateCatlog(catlog: Catlog)
}