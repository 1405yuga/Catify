package com.example.catify.ui.shopping_cart_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.catify.databinding.CardShoppingItemBinding
import com.example.catify.entity.CartItem

class ShoppingCartListAdapter :
    ListAdapter<CartItem, ShoppingCartListAdapter.ShoppingCartViewHolder>(DiffCallBack) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }

    class ShoppingCartViewHolder(private val binding: CardShoppingItemBinding) :
        ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.itemNameEditText.setText(cartItem.itemName)
            binding.stock.text = cartItem.stock.toString()
            binding.itemCheckBox.isChecked = cartItem.isPurchased
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        return ShoppingCartViewHolder(
            CardShoppingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}