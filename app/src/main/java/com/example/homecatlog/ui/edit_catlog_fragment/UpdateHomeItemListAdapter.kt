package com.example.homecatlog.ui.edit_catlog_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homecatlog.databinding.CardUpdateHomeItemBinding
import com.example.homecatlog.entity.HomeItem

class UpdateHomeItemListAdapter(private val increaseQty: (String) -> (Int)) :
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
            increaseQty: (String) -> (Int)
        ) {
            binding.apply {
                itemName.text = homeItem.itemName
                quantity.text = homeItem.availableStock.toString()
                addQuantity.setOnClickListener {
                    quantity.text = increaseQty(homeItem.itemName).toString()
                }
//                subQuantity.setOnClickListener {
//                    updateQuantity(
//                        category,
//                        homeItem.itemName,
//                        homeItem.availableStock - 1
//                    )
//                }
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
        holder.bind(getItem(position), increaseQty)
    }
}