package com.example.homecatlog.listfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homecatlog.R
import com.example.homecatlog.databinding.CardItemDetailsBinding
import com.example.homecatlog.databinding.FragmentAddCatlogBinding
import com.example.homecatlog.entity.HomeItem

class AddCatalogFragment : Fragment() {

    private lateinit var binding: FragmentAddCatlogBinding
    private val TAG = this.javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCatlogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
    }

    private fun applyBinding() {
        binding.apply {
            backButton.setOnClickListener { navigateToBackFragment() }
            addItem.setOnClickListener { addItemDetailsView() }
            saveButton.setOnClickListener { getAllItemDetails() }
        }
    }

    private fun getAllItemDetails(): List<HomeItem> {
        val itemDetailsList = mutableListOf<HomeItem>()
        for (i in 0 until binding.cardContainer.childCount) {
            val cardItemDetailsBinding =
                CardItemDetailsBinding.bind(binding.cardContainer.getChildAt(i))
            val homeItem = HomeItem(
                cardItemDetailsBinding.itemName.editText?.text.toString(),
                cardItemDetailsBinding.qtyAvailable.editText?.text.toString().toInt()
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