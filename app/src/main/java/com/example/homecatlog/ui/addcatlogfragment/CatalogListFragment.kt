package com.example.homecatlog.ui.addcatlogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.homecatlog.R
import com.example.homecatlog.databinding.FragmentCatalogListBinding
import com.example.homecatlog.network.BaseApplication
import com.example.homecatlog.ui.CatalogViewModel

class CatalogListFragment : Fragment() {

    private lateinit var binding: FragmentCatalogListBinding
    private val TAG = this.javaClass.simpleName
    private val viewModel: CatalogViewModel by activityViewModels {
        CatalogViewModel.CatalogViewModelFactory((activity?.application as BaseApplication).database.getCatalogDao())
    }
    private lateinit var catalogListAdapter: CatalogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogListBinding.inflate(layoutInflater, container, false)
        catalogListAdapter = CatalogListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding()
        viewModel.allCatalogs.observe(viewLifecycleOwner) {
            Log.d(TAG, "Catalogs list :${it.size}")
            if (it.isEmpty()) {
                binding.apply {
                    emptyListText.visibility = View.VISIBLE
                    categoryRecyclerView.visibility = View.GONE
                }
            } else {
                binding.apply {
                    emptyListText.visibility = View.GONE
                    categoryRecyclerView.visibility = View.VISIBLE
                    catalogListAdapter.submitList(it)
                }
            }
        }
    }

    private fun applyBinding() {
        binding.apply {
            addCatlog.setOnClickListener { navigateToFragment(R.id.addCatlogFragment) }
            categoryRecyclerView.adapter = catalogListAdapter
        }
    }

    private fun navigateToFragment(fragmentId: Int) {
        findNavController().apply {
            navigate(fragmentId)
        }
    }
}