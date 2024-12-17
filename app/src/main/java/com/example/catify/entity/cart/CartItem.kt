package com.example.catify.entity.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val itemId: Int,
    var itemName: String,
    var stock: Int,
    var isPurchased: Boolean
) {}
