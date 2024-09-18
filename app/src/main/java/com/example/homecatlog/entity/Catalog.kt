package com.example.homecatlog.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Catalog(
    @PrimaryKey
    val category: String,
    val homeItems: List<HomeItem>
)
