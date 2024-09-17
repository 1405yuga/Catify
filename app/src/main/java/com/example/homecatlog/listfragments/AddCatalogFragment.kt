package com.example.homecatlog.listfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homecatlog.R
import com.example.homecatlog.databinding.FragmentAddCatlogBinding

class AddCatalogFragment : Fragment() {

    private lateinit var binding: FragmentAddCatlogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCatlogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backButton.setOnClickListener { navigateToBackFragment() }
            addItem.setOnClickListener { addItemDetailsView() }
        }
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