package com.example.catify.ui.edit_catlog_fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catify.R
import com.example.catify.databinding.CardUpdateHomeItemBinding
import com.example.catify.entity.HomeItem

class UpdateHomeItemListAdapter(
    private val increaseQty: (String) -> (Int),
    private val decreaseQty: (String) -> (Int),
    private val addItemView: () -> (Unit)
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
            decreaseQty: (String) -> Int,
            addItemView: () -> Unit
        ) {
            val TAG = this.javaClass.simpleName
            Log.d(TAG, "ViewHolder called -----")

            binding.apply {
                itemName.apply {
                    setText(homeItem.itemName)

                    //move to next edit text
                    itemName.setOnKeyListener { view, i, keyEvent ->
                        if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                            val nextPos = adapterPosition + 1
                            val recyclerView = itemView.parent as? RecyclerView
                            val adapter = recyclerView?.adapter as? UpdateHomeItemListAdapter
                            if (adapter != null) {
                                if (nextPos < adapter.itemCount) {
                                    recyclerView.findViewHolderForAdapterPosition(nextPos)?.itemView?.findViewById<EditText>(
                                        R.id.item_name
                                    )?.requestFocus()

                                } else {
                                    addItemView()
                                }
                            }
                            true
                        } else false
                    }

                    //set focus to last character
                    itemName.setOnFocusChangeListener { view, hasFocus ->
                        if (hasFocus) {
                            val editText = view as EditText
                            editText.setSelection(editText.length())
                        }
                    }

                    //set entered value
                    itemName.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                            if (p0!!.length == 1) homeItem.itemName = p0.toString()
                            Log.d(TAG, "before $p0")
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            if (p0!!.length > 1) homeItem.itemName = p0.toString()
                            Log.d(TAG, "onTextChanged $p0")
                        }

                        override fun afterTextChanged(p0: Editable?) {}

                    })
                }
                quantity.text = homeItem.availableStock.toString()
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
        holder.bind(getItem(position), increaseQty, decreaseQty, addItemView)
    }
}