package com.example.catify.ui.edit_catlog_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.catify.R
import com.example.catify.databinding.FragmentUpdateCatalogBinding
import com.example.catify.entity.Catalog
import com.example.catify.entity.HomeItem
import com.example.catify.helper.Converters
import com.example.catify.network.BaseApplication
import com.example.catify.ui.CatalogViewModel
import com.google.android.material.snackbar.Snackbar

class UpdateCatalogFragment : Fragment() {
    private lateinit var binding: FragmentUpdateCatalogBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var catalog: Catalog
    private lateinit var updateHomeItemListAdapter: UpdateHomeItemListAdapter
    private val viewModel: UpdateCatalogViewModel by activityViewModels()
    private lateinit var swipeHelper: ItemTouchHelper
    private val catalogViewModel: CatalogViewModel by activityViewModels {
        CatalogViewModel.CatalogViewModelFactory((activity?.application as BaseApplication).database.getCatalogDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val catalogJson = arguments?.getString("CATALOG")
        catalog = if (catalogJson != null) Converters().jsonToCatalog(catalogJson)
        else Catalog(category = "", homeItems = mutableListOf(HomeItem("", 0)))
        Log.d(TAG, "Catalog : $catalog")
        viewModel.initialise(catalog = catalog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateCatalogBinding.inflate(layoutInflater, container, false)
        updateHomeItemListAdapter =
            UpdateHomeItemListAdapter(increaseQty = { viewModel.increaseQuantity(it) },
                decreaseQty = { viewModel.decreaseQuantity(it) },
                addItemView = {
                    catalog = viewModel.addHomeItemView()
                    updateHomeItemListAdapter.submitList(catalog.homeItems) {
                        Log.d(TAG, "Added blank homeitem - ${catalog.homeItems.size}")

                        val pos = catalog.homeItems.size - 1
                        binding.homeItemsRecyclerView.post {
                            binding.homeItemsRecyclerView.findViewHolderForAdapterPosition(pos)?.itemView?.findViewById<EditText>(
                                R.id.item_name
                            )?.requestFocus()
                        }
                    }
                })
        updateHomeItemListAdapter.submitList(catalog.homeItems)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
        applyDeleteOnSwipe()
    }

    private fun applyDeleteOnSwipe() {
        swipeHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val homeItem = updateHomeItemListAdapter.currentList[position]
                if (updateHomeItemListAdapter.currentList.size > 1) {
                    catalog = viewModel.removeHomeItem(viewHolder.adapterPosition)
                    updateHomeItemListAdapter.submitList(catalog.homeItems)
                } else updateHomeItemListAdapter.notifyItemChanged(viewHolder.adapterPosition)

                Snackbar.make(
                    binding.homeItemsRecyclerView,
                    homeItem.itemName,
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Undo") {
                        catalog = viewModel.reAddHomeItem(position, homeItem)
                        updateHomeItemListAdapter.submitList(catalog.homeItems)
                    }.show()
            }
        })
        swipeHelper.attachToRecyclerView(binding.homeItemsRecyclerView)
    }

    private fun applyBinding() {
        binding.apply {
            categoryText.setText(catalog.category)
            homeItemsRecyclerView.adapter = updateHomeItemListAdapter
            saveButton.setOnClickListener {
                val categoryText = binding.categoryText.text.toString().trim()
                if (categoryText.isBlank()) binding.categoryText.error = "Category required!"
                else {
                    catalog.category = categoryText
                    catalog.homeItems = viewModel.removeEmptyHomeItem()
                    catalogViewModel.addCatalog(catalog = catalog,
                        onSuccess = { navigateToBackFragment() },
                        onFailure = {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        })
                }

            }
            backButton.setOnClickListener { navigateToBackFragment() }
            categoryText.apply {
                requestFocus()
                setSelection(this.length())
            }
        }
    }

    private fun navigateToBackFragment() {
        findNavController().popBackStack()
    }
}