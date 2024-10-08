package com.example.homecatlog.ui.edit_catlog_fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homecatlog.databinding.CardUpdateHomeItemBinding
import com.example.homecatlog.entity.HomeItem

class UpdateHomeItemListAdapter(
    private val increaseQty: (String) -> (Int),
    private val decreaseQty: (String) -> (Int)
) :
    ListAdapter<HomeItem, UpdateHomeItemListAdapter.HomeItemViewHolder>(DiffCallBack) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<HomeItem>() {
            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem.itemName == newItem.itemName
            }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class HomeItemViewHolder(private val binding: CardUpdateHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            homeItem: HomeItem,
            increaseQty: (String) -> Int,
            decreaseQty: (String) -> Int
        ) {
            val TAG = this.javaClass.simpleName
            binding.apply {
                itemName.setText(homeItem.itemName)
                quantity.text = homeItem.availableStock.toString()
                itemName.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0!!.length == 1) homeItem.itemName = p0.toString()
                        Log.d(TAG, "before $p0")
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0!!.length > 1) homeItem.itemName = p0.toString()
                        Log.d(TAG, "onTextChanged $p0")
                    }

                    override fun afterTextChanged(p0: Editable?) {}

                })
                addQuantity.setOnClickListener {
                    quantity.text = increaseQty(homeItem.itemName).toString()
                }
                subQuantity.setOnClickListener {
                    quantity.text = decreaseQty(homeItem.itemName).toString()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        return HomeItemViewHolder(
            CardUpdateHomeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.bind(getItem(position), increaseQty, decreaseQty)
    }
}