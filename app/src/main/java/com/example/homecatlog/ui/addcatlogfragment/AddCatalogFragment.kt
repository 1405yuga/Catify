package com.example.homecatlog.ui.addcatlogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.homecatlog.R
import com.example.homecatlog.databinding.CardItemDetailsBinding
import com.example.homecatlog.databinding.FragmentAddCatlogBinding
import com.example.homecatlog.entity.HomeItem
import com.example.homecatlog.network.BaseApplication
import com.example.homecatlog.ui.CatalogViewModel

class AddCatalogFragment : Fragment() {

    private lateinit var binding: FragmentAddCatlogBinding
    private val TAG = this.javaClass.simpleName
    private val viewModel: CatalogViewModel by activityViewModels {
        CatalogViewModel.CatalogViewModelFactory((activity?.application as BaseApplication).database.getCatalogDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCatlogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addItemDetailsView()
        applyBinding()
    }

    private fun applyBinding() {
        binding.apply {
            backButton.setOnClickListener { navigateToBackFragment() }
            addItem.setOnClickListener { addItemDetailsView() }
            saveButton.setOnClickListener {
                viewModel.addCatalog(
                    category = binding.categoryEditText.text.toString().trim(),
                    homeItems = getAllItemDetails(),
                    onSuccess = {
                        Toast.makeText(requireContext(), "Added catalog $it", Toast.LENGTH_SHORT)
                            .show()
                        navigateToBackFragment()
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), "Try again. $it", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun getAllItemDetails(): List<HomeItem> {
        val itemDetailsList = mutableListOf<HomeItem>()
        for (i in 0 until binding.cardContainer.childCount) {
            val cardItemDetailsBinding =
                CardItemDetailsBinding.bind(binding.cardContainer.getChildAt(i))
            val itemName = cardItemDetailsBinding.itemName.editText?.text.toString().trim()

            //if blank item then dont add
            if (itemName.isBlank()) continue
            cardItemDetailsBinding.itemName.error = null

            val homeItem = HomeItem(
                cardItemDetailsBinding.itemName.editText?.text.toString(),
                cardItemDetailsBinding.qtyAvailable.editText?.text.toString().toIntOrNull() ?: 0
            )
            itemDetailsList.add(homeItem)
        }
        Log.d(TAG, "Item details card :" + itemDetailsList.size)
        return itemDetailsList
    }

    private fun addItemDetailsView() {
        val inflater = LayoutInflater.from(requireContext())
        val itemDetailsView =
            inflater.inflate(R.layout.card_item_details, binding.cardContainer, false)
        binding.cardContainer.addView(itemDetailsView)
    }

    private fun navigateToBackFragment() {
        findNavController().popBackStack()
    }
}