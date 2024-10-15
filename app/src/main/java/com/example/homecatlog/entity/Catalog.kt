package com.example.homecatlog.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Catalog(
    @PrimaryKey(autoGenerate = true)
    val catalogId: Int = 0,
    var category: String,
    val homeItems: List<HomeItem>
)
