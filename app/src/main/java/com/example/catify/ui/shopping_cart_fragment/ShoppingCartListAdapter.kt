package com.example.catify.ui.shopping_cart_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.catify.ShoppingCartProto
import com.example.catify.databinding.CardShoppingItemBinding

class ShoppingCartListAdapter(private val updateStock: (Int, Int) -> Unit) :
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
            cartItem: ShoppingCartProto.CartItem,
            updateStock: (Int, Int) -> (Unit)
        ) {
            binding.itemNameEditText.setText(cartItem.itemName)
            binding.stock.text = cartItem.stock.toString()
            binding.itemCheckBox.isChecked = cartItem.isPurchased
            var stock = cartItem.stock
            binding.addQuantity.setOnClickListener {
                stock++
                binding.stock.text = stock.toString()
                updateStock(adapterPosition, stock)
            }
            binding.subQuantity.setOnClickListener {
                if (stock > 0) stock--
                binding.stock.text = stock.toString()
                updateStock(adapterPosition, stock)
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
        holder.bind(getItem(position), updateStock = updateStock)
    }
}