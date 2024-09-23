package com.example.homecatlog.ui.listfragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homecatlog.databinding.CardHomeItemBinding
import com.example.homecatlog.entity.HomeItem

class HomeItemListAdapter(
    private val category: String,
    private val updateQuantity: (String, String, Int) -> (Unit)
) :
    ListAdapter<HomeItem, HomeItemListAdapter.HomeItemViewHolder>(DiffCallBack) {
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

    class HomeItemViewHolder(private val binding: CardHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            homeItem: HomeItem,
            category: String,
            updateQuantity: (String, String, Int) -> Unit
        ) {
            binding.apply {
                itemName.text = homeItem.itemName
                quantity.text = homeItem.availableStock.toString()
                addQuantity.setOnClickListener {
                    updateQuantity(
                        category,
                        homeItem.itemName,
                        homeItem.availableStock+1
                    )
                }
                subQuantity.setOnClickListener { updateQuantity(
                    category,
                    homeItem.itemName,
                    homeItem.availableStock-1
                ) }
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
        holder.bind(getItem(position), category, updateQuantity)
    }
}