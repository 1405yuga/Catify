package com.example.catify.ui.home_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.catify.R
import com.example.catify.databinding.FragmentCatalogListBinding
import com.example.catify.helper.Converters
import com.example.catify.network.BaseApplication
import com.example.catify.ui.CatalogViewModel
import com.example.catify.ui.layout_store.LayoutPreference
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CatalogListFragment : Fragment() {

    private lateinit var binding: FragmentCatalogListBinding
    private val TAG = this.javaClass.simpleName
    private lateinit var swipeHelper: ItemTouchHelper
    private var isLayoutLinear = false
    private lateinit var layoutPreference: LayoutPreference
    private val viewModel: CatalogViewModel by activityViewModels {
        CatalogViewModel.CatalogViewModelFactory((activity?.application as BaseApplication).database.getCatalogDao())
    }
    private lateinit var catalogListAdapter: CatalogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogListBinding.inflate(layoutInflater, container, false)
        catalogListAdapter = CatalogListAdapter(
            maxItemsDislayed = 5, navigateToEdit = {
                val bundle = Bundle().apply { putString("CATALOG", Converters().catalogToJson(it)) }
                findNavController().navigate(
                    R.id.updateCatalogFragment,
                    args = bundle
                )
            })
        layoutPreference = LayoutPreference(requireContext())
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

                //remove actlog
                viewModel.removeCatalog(catalog,
                    onSuccess = { Log.d(TAG, "Catlog deleted") },
                    onFailure = { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() })

                //undo remove
                Snackbar.make(
                    binding.categoryRecyclerView,
                    "${catalog.category} deleted",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("Undo", View.OnClickListener {
                        viewModel.addCatalog(catalog,
                            onSuccess = { Log.d(TAG, "Catlog re-added") },
                            onFailure = {
                                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                            })
                    })
                    .show()
            }

        })
        swipeHelper.attachToRecyclerView(binding.categoryRecyclerView)
    }

    private fun applyObservers() {
        viewModel.allCatalogs.observe(viewLifecycleOwner) {
            Log.d(TAG, "Catalogs list :${it.size}")
            if (it.isEmpty()) {
                binding.apply {
                    emptyListLayout.visibility = View.VISIBLE
                    categoryRecyclerView.visibility = View.GONE
                }
            } else {
                binding.apply {
                    emptyListLayout.visibility = View.GONE
                    categoryRecyclerView.visibility = View.VISIBLE
                    catalogListAdapter.submitList(it)
                }
            }
        }

        layoutPreference.userLayoutPreference.asLiveData().observe(viewLifecycleOwner) {
            isLayoutLinear = it
            Log.d(TAG, "Preference isLinear : $it")
            binding.apply {
                if (!isLayoutLinear) {
                    categoryRecyclerView.layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    topAppBar.menu.findItem(R.id.layout_manager).icon =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.linear_layout_24
                        )
                } else {
                    categoryRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                    topAppBar.menu.findItem(R.id.layout_manager).icon =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.staggered_layout_24
                        )
                }
            }
        }
    }

    private fun applyBinding() {
        binding.apply {
            addCatlog.setOnClickListener { navigateToFragment(R.id.updateCatalogFragment) }
            categoryRecyclerView.adapter = catalogListAdapter
            topAppBar.setNavigationOnClickListener { drawerLayout.open() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.layout_manager -> {
                        isLayoutLinear = !isLayoutLinear
                        lifecycleScope.launch {
                            layoutPreference.saveLayoutPreference(requireContext(), isLayoutLinear)
                        }
                        true
                    }

                    else -> false
                }
            }
            navDrawer.apply {
                documentation.setOnClickListener {
                    openUrl(
                        "https://github.com/1405yuga/HomeCatalogs/blob/main/README.md",
                        binding
                    )
                }
                about.setOnClickListener { openUrl("https://github.com/1405yuga/", binding) }
            }
        }
    }

    private fun openUrl(url: String, binding: FragmentCatalogListBinding) {
        binding.drawerLayout.close()
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun navigateToFragment(fragmentId: Int) {
        findNavController().apply {
            navigate(fragmentId)
        }
    }
}