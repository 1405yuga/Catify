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
import com.example.catify.databinding.CardUpdateHomeItemBinding
import com.example.catify.entity.CatalogListItem

class UpdateHomeItemListAdapter(
    private val addItemView: (Int, String) -> (Unit),
    private val removeItemView: (currentIndex: Int) -> Unit
) :
    ListAdapter<CatalogListItem, UpdateHomeItemListAdapter.HomeItemViewHolder>(DiffCallBack) {
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<CatalogListItem>() {
            override fun areItemsTheSame(
                oldItem: CatalogListItem,
                newItem: CatalogListItem
            ): Boolean {
                return oldItem.itemName == newItem.itemName
            }

            override fun areContentsTheSame(
                oldItem: CatalogListItem,
                newItem: CatalogListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class HomeItemViewHolder(
        private val binding: CardUpdateHomeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val TAG = this.javaClass.simpleName

        fun bind(
            catalogListItem: CatalogListItem,
            addItemView: (Int, String) -> Unit,
            removeItemView: (currentIndex: Int) -> Unit
        ) {
            Log.d(TAG, "ViewHolder called -----")
            binding.itemName.setText(catalogListItem.itemName)
            binding.itemName.setOnKeyListener { view, i, keyEvent ->
                //on enter press - add new textview with remaining text
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    val nextPos = adapterPosition + 1
                    val cursorPos = binding.itemName.selectionStart
                    val mainText = catalogListItem.itemName.slice(0 until cursorPos)
                    val remainingText =
                        catalogListItem.itemName.slice(cursorPos until catalogListItem.itemName.length)
                    Log.d(TAG, remainingText)
                    addItemView(nextPos, remainingText)
                    binding.itemName.setText(mainText)
                    true
                } else if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_DEL) {
                    if (binding.itemName.selectionStart == 0 && adapterPosition > 0) {
                        removeItemView(adapterPosition)
                    }
                    true
                } else false
            }

            //set focus to last character
            binding.itemName.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    val editText = view as EditText
                    editText.setSelection(editText.length())
                }
            }

            //set entered value
            binding.itemName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.trim().isNotEmpty()) catalogListItem.itemName = p0.toString()
                    Log.d(TAG, "onTextChanged $p0")
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            fun refreshCount() {
                binding.quantity.text = catalogListItem.availableStock.toString()
            }
            binding.addQuantity.setOnClickListener {
                catalogListItem.availableStock++
                refreshCount()
            }
            binding.subQuantity.setOnClickListener {
                catalogListItem.decreaseQty()
                refreshCount()
            }
            refreshCount()
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
        holder.bind(
            catalogListItem = getItem(position),
            addItemView = addItemView,
            removeItemView = removeItemView
        )
    }
}