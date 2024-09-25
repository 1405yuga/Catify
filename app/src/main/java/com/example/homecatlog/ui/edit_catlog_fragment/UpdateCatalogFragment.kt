package com.example.homecatlog.ui.edit_catlog_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.homecatlog.databinding.FragmentUpdateCatalogBinding
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.helper.Converters
import com.example.homecatlog.network.BaseApplication
import com.example.homecatlog.ui.CatalogViewModel

class UpdateCatalogFragment : Fragment() {
    private lateinit var binding: FragmentUpdateCatalogBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var catalog: Catalog
    private lateinit var updateHomeItemListAdapter: UpdateHomeItemListAdapter
    private val viewModel: UpdateCatalogViewModel by activityViewModels()
    private val catalogViewModel: CatalogViewModel by activityViewModels {
        CatalogViewModel.CatalogViewModelFactory((activity?.application as BaseApplication).database.getCatalogDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val catalogJson = arguments?.getString("CATALOG")
        catalog = Converters().jsonToCatalog(catalogJson!!)
        Log.d(TAG, "Catalog : $catalog")
        viewModel.initialise(catalog = catalog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateCatalogBinding.inflate(layoutInflater, container, false)
        viewModel.initialise(catalog = catalog)
        updateHomeItemListAdapter =
            UpdateHomeItemListAdapter(increaseQty = { viewModel.increaseQuantity(it) },
                decreaseQty = { viewModel.decreaseQuantity(it) })
        updateHomeItemListAdapter.submitList(catalog.homeItems)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
    }

    private fun applyBinding() {
        binding.apply {
            categoryText.text = catalog.category
            homeItemsRecyclerView.adapter = updateHomeItemListAdapter
        }
    }
}