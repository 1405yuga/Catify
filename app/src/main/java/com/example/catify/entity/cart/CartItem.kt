package com.example.catify.entity.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    var itemName: String,
    var stock: Int,
    var isPurchased: Boolean
) {}
