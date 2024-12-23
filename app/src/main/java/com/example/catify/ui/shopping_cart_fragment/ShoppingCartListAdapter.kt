package com.example.catify.ui.shopping_cart_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.catify.ShoppingCartProto
import com.example.catify.databinding.CardShoppingItemBinding

class ShoppingCartListAdapter() :
    ListAdapter<ShoppingCartProto.CartItem, ShoppingCartListAdapter.ShoppingCartViewHolder>(
        DiffCallBack
    ) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<ShoppingCartProto.CartItem>() {
            override fun areItemsTheSame(
                oldItem: ShoppingCartProto.CartItem,
                newItem: ShoppingCartProto.CartItem
            ): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(
                oldItem: ShoppingCartProto.CartItem,
                newItem: ShoppingCartProto.CartItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ShoppingCartViewHolder(private val binding: CardShoppingItemBinding) :
        ViewHolder(binding.root) {
        fun bind(
            cartItem: ShoppingCartProto.CartItem
        ) {
            binding.itemNameEditText.setText(cartItem.itemName)
            binding.stock.text = cartItem.stock.toString()
            binding.itemCheckBox.isChecked = cartItem.isPurchased
            binding.addQuantity.setOnClickListener {
                // TODO: increase stock by 1
            }
            binding.subQuantity.setOnClickListener {
                // TODO: decrease stock by 1
            }
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