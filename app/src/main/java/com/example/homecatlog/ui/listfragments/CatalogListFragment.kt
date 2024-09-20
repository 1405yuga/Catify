package com.example.homecatlog.ui.listfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.homecatlog.R
import com.example.homecatlog.databinding.FragmentCatalogListBinding
import com.example.homecatlog.network.BaseApplication
import com.example.homecatlog.ui.CatalogViewModel

class CatalogListFragment : Fragment() {

    private lateinit var binding: FragmentCatalogListBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var swipeHelper: ItemTouchHelper
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
        applyObservers()
        applyDeleteOnSwipe()
    }

    private fun applyDeleteOnSwipe() {
        swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val catalog = catalogListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeCatalog(catalog,
                    onSuccess = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() },
                    onFailure = { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() })
            }

        })
        swipeHelper.attachToRecyclerView(binding.categoryRecyclerView)
    }

    private fun applyObservers() {
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