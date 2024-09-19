package com.example.homecatlog.ui.listfragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homecatlog.databinding.CardHomeItemBinding
import com.example.homecatlog.entity.HomeItem

class HomeItemListAdapter() :
    ListAdapter<HomeItem, HomeItemListAdapter.HomeItemViewHolder>(DiffCallBack) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<HomeItem>() {
            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class HomeItemViewHolder(private val binding: CardHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(homeItem: HomeItem) {
            binding.apply {
                itemName.text = homeItem.itemName
                quantity.text = homeItem.availableStock.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        return HomeItemViewHolder(
            CardHomeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}