package com.example.catify.ui.shopping_cart_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.network.cart_data.ShoppingCartRepository
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val cartRepository: ShoppingCartRepository) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    var tempCartItemsList: MutableList<CartItem>? = null

    var onDataLoaded: ((MutableList<CartItem>) -> (Unit))? = null

    init {
        viewModelScope.launch {
            try {
                tempCartItemsList = cartRepository.getInitialCart().cartItemListList.toMutableList()
                Log.d(TAG, "Data populated : $tempCartItemsList")
                onDataLoaded?.invoke(tempCartItemsList!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveShoppingCart() {
        viewModelScope.launch { tempCartItemsList?.let { cartRepository.addAllItems(cartItemsList = it) } }
    }

    companion object {
        fun provideFactory(cartRepository: ShoppingCartRepository): Factory {
            return Factory(cartRepository)
        }

        class Factory(
            private val cartRepository: ShoppingCartRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ShoppingCartViewModel(cartRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}

