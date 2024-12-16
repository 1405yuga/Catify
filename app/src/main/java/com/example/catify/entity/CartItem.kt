package com.example.catify.entity

data class CartItem(
    val itemId: Int,
    var itemName: String,
    var stock: Int,
    var isPurchased: Boolean
) {}
