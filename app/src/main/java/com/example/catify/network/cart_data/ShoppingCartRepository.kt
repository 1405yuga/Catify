package com.example.catify.network.cart_data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.ShoppingCartProto.ShoppingCart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class ShoppingCartRepository(private val cartDataStore: DataStore<ShoppingCart>) {
    private val TAG = this.javaClass.simpleName

//
//    private suspend fun updateShoppingCart(update: (currentCart: ShoppingCart) -> ShoppingCart) {
//        cartDataStore.updateData { currentCart ->
//            update(currentCart)
//        }
//    }

    val shoppingCartFlow: Flow<ShoppingCart> = cartDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d(TAG, "Error while getting shopping list : $exception")
                emit(ShoppingCart.getDefaultInstance())

            } else {
                throw exception
            }
        }


    suspend fun addItem(position: Int, cartItem: CartItem) {
        cartDataStore.updateData { currentCart ->
            currentCart.toBuilder()
                .addCartItemList(position, cartItem).build()
        }
    }

    suspend fun removeAt(position: Int) {
        cartDataStore.updateData { currentCart ->
            currentCart.toBuilder()
                .removeCartItemList(position).build()
        }
    }

    suspend fun increaseQty(position: Int) {
        cartDataStore.updateData { currentCart ->
            val cartItem = currentCart.getCartItemList(position)
            currentCart.toBuilder().setCartItemList(
                position,
                cartItem.toBuilder().setStock(cartItem.stock + 1).build()
            ).build()
        }
    }

    suspend fun addAllItems(cartItemsList: MutableList<CartItem>) {
        cartDataStore.updateData { currentCart ->
            currentCart
                .toBuilder()
                .addAllCartItemList(cartItemsList)
                .build()
        }
    }


}