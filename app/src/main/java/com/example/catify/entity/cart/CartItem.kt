package com.example.catify.entity.cart

data class CartItem(
    val itemId: Int,
    var itemName: String,
    var stock: Int,
    var isPurchased: Boolean
) {}
