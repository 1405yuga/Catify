package com.example.homecatlog.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Catlog")
data class Catlog(
    @PrimaryKey val category: String,
    val homeItems: List<HomeItem>
)
