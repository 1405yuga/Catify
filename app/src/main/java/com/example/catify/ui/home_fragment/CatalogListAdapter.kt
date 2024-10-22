package com.example.catify.ui.home_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catify.databinding.CardCatalogItemBinding
import com.example.catify.entity.Catalog

class CatalogListAdapter(
    private val navigateToEdit: (catalog: Catalog) -> (Unit)
) :
    ListAdapter<Catalog, CatalogListAdapter.CatalogViewHolder>(DiffCallBack) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Catalog>() {
            override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
                return oldItem.category == newItem.category
            }

            override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean {
                return oldItem == newItem
            }
        }
    }

    class CatalogViewHolder(private val binding: CardCatalogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            catalog: Catalog,
            navigateToEdit: (catalog: Catalog) -> (Unit)
        ) {
            val viewHomeItemListAdapter = ViewHomeItemListAdapter()
            viewHomeItemListAdapter.submitList(catalog.homeItems)
            binding.apply {
                catlogCard.setOnClickListener { navigateToEdit(catalog) }
                categoryTextView.text = catalog.category
                homeItemsRecyclerView.apply {
                    adapter = viewHomeItemListAdapter
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder(
            CardCatalogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(getItem(position), navigateToEdit)
    }
}