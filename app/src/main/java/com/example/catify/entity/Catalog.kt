package com.example.catify.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Catalog(
    @PrimaryKey(autoGenerate = true)
    val catalogId: Int = 0,
    var category: String,
    var homeItems: List<HomeItem>
)
