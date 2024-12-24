package com.example.catify.network.cart_data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.ShoppingCartProto.ShoppingCart
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

class ShoppingCartRepository(private val cartDataStore: DataStore<ShoppingCart>) {
    private val TAG = this.javaClass.simpleName

    suspend fun getInitialCart(): ShoppingCart {
        return cartDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(ShoppingCart.getDefaultInstance())
            } else {
                throw exception
            }
        }.first()
    }

    suspend fun addAllItems(cartItemsList: MutableList<CartItem>) {
        try {
            cartDataStore.updateData { currentCart ->
                Log.d(TAG, "List received in repo : ${cartItemsList.size}")
                currentCart
                    .toBuilder()
                    .clearCartItemList()
                    .addAllCartItemList(cartItemsList)
                    .build()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating data: ${e.message}")
        }
    }


}