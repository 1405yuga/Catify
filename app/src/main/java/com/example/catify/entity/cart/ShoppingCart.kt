package com.example.catify.entity.cart

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingCart(
    val cartItemList: MutableList<CartItem>
)
