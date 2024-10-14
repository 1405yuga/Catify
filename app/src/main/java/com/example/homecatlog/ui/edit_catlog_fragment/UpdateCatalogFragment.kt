package com.example.homecatlog.ui.edit_catlog_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        updateHomeItemListAdapter =
            UpdateHomeItemListAdapter(increaseQty = { viewModel.increaseQuantity(it) },
                decreaseQty = { viewModel.decreaseQuantity(it) },
                addItemView = {
                    catalog = viewModel.addHomeItemView()!!
                    updateHomeItemListAdapter.submitList(catalog.homeItems)
                    Log.d(TAG, "Added view $catalog")
                })
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
            saveButton.setOnClickListener {
                catalogViewModel.updateHomeItemQuantity(
                    updatedCatalog = catalog,
                    onSuccess = { navigateToBackFragment() },
                    onFailure = {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    })
            }
            backButton.setOnClickListener { navigateToBackFragment() }
        }
    }

    private fun navigateToBackFragment() {
        findNavController().popBackStack()
    }
}