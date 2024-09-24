package com.example.homecatlog.ui.edit_catlog_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.homecatlog.databinding.FragmentUpdateCatalogBinding
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.helper.Converters

class UpdateCatalogFragment : Fragment() {

    private lateinit var binding: FragmentUpdateCatalogBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var catalog: Catalog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val catalogJson = arguments?.getString("CATALOG")
        catalog = Converters().jsonToCatalog(catalogJson!!)
        Log.d(TAG, "Catalog : $catalog")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateCatalogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}