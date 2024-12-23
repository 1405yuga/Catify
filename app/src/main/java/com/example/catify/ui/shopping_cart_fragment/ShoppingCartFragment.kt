package com.example.catify.ui.shopping_cart_fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.ShoppingCartProto.ShoppingCart
import com.example.catify.databinding.FragmentShoppingCartBinding
import com.example.catify.network.cart_data.ShoppingCartRepository
import com.example.catify.network.cart_data.ShoppingCartSerializer
import com.google.android.material.snackbar.Snackbar


private val Context.shoppingCartDataStore: DataStore<ShoppingCart> by dataStore(
    fileName = "shopping_cart.pb",
    serializer = ShoppingCartSerializer
)

class ShoppingCartFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: FragmentShoppingCartBinding
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var shoppingCartListAdapter: ShoppingCartListAdapter
    private lateinit var swipeHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(layoutInflater, container, false)
        shoppingCartRepository = ShoppingCartRepository(requireContext().shoppingCartDataStore)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingCartViewModel = ViewModelProvider(
            this,
            ShoppingCartViewModel.provideFactory((shoppingCartRepository))
        )[ShoppingCartViewModel::class.java]
        shoppingCartListAdapter = ShoppingCartListAdapter(
            increaseQty = { shoppingCartViewModel.increaseQuantity(it) }
        )
        applyBindings()
        applyDeleteOnSwipe()
        applyObservers()
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
                val cartItem = shoppingCartListAdapter.currentList[position]
                shoppingCartViewModel.deleteItemAt(position = position)
                Log.d(TAG, "DELETED at : $position")
                Snackbar.make(
                    binding.shoppingListRecyclerView,
                    "${cartItem.itemName} removed",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    shoppingCartViewModel.addCartItemAt(position = position, item = cartItem)
                }
                    .show()
            }

        })
        swipeHelper.attachToRecyclerView(binding.shoppingListRecyclerView)
    }

    private fun applyObservers() {
        shoppingCartViewModel.shoppingCart.observe(viewLifecycleOwner) {
            Log.d(TAG, "Shopping Cart: ${it.cartItemListList}")
            // TODO: changes not reflected on ui
            shoppingCartListAdapter.submitList(it.cartItemListList.toList())
        }
    }

    private fun applyBindings() {
        binding.shoppingListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.shoppingListRecyclerView.adapter = shoppingCartListAdapter
        binding.addItemTextButton.setOnClickListener {
            shoppingCartViewModel.addCartItemAt(
                position = 0,
                item = CartItem.getDefaultInstance()
            )
        }
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }
}