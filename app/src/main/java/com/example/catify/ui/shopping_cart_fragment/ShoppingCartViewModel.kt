package com.example.catify.ui.shopping_cart_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.ShoppingCartProto.ShoppingCart
import com.example.catify.network.cart_data.ShoppingCartRepository
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val cartRepository: ShoppingCartRepository) : ViewModel() {
    private val TAG = this.javaClass.simpleName

    val shoppingCart: LiveData<ShoppingCart> = cartRepository.shoppingCartFlow.asLiveData()

    fun addCartItemAt(position: Int, item: CartItem) {
        viewModelScope.launch { cartRepository.addItem(position = position, cartItem = item) }
    }

    fun increaseQuantity(position: Int) {
        viewModelScope.launch { cartRepository.increaseQty(position = position) }
    }

    fun deleteItemAt(position: Int) {
        viewModelScope.launch {
            cartRepository.removeAt(position)
        }
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

