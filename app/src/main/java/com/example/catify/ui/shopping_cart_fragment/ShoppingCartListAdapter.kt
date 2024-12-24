package com.example.catify.ui.shopping_cart_fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.catify.ShoppingCartProto
import com.example.catify.databinding.CardShoppingItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShoppingCartListAdapter(
    private val updateIsPurchased: (Int, Boolean) -> Unit,
    private val updateItemName: (Int, String) -> (Unit),
    private val updateStock: (Int, Int) -> Unit
) :
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
        val TAG = this.javaClass.simpleName
        fun bind(
            cartItem: ShoppingCartProto.CartItem,
            updateIsPurchased: (Int, Boolean) -> (Unit),
            updateItemName: (Int, String) -> (Unit),
            updateStock: (Int, Int) -> (Unit)
        ) {
            binding.itemNameEditText.setText(cartItem.itemName)
            binding.stock.text = cartItem.stock.toString()
            binding.itemCheckBox.isChecked = cartItem.isPurchased
            binding.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (cartItem.isPurchased != isChecked) {
                    updateIsPurchased(adapterPosition, isChecked)
                    Log.d(TAG,"isPurchased value : $isChecked")
                }
            }
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
            binding.itemNameEditText.addTextChangedListener(object : TextWatcher {
                private var doJob: Job? = null
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    doJob?.cancel()
                    doJob = CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        p0?.let {
                            Log.d(TAG, "AFTER text changed $p0")
                            updateItemName(adapterPosition, p0.toString())
                        }
                    }
                }
            })
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
        holder.bind(
            getItem(position), updateItemName = updateItemName, updateStock = updateStock,
            updateIsPurchased = updateIsPurchased
        )
    }
}