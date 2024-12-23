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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catify.ShoppingCartProto
import com.example.catify.ShoppingCartProto.CartItem
import com.example.catify.ShoppingCartProto.ShoppingCart
import com.example.catify.databinding.FragmentShoppingCartBinding
import com.example.catify.network.cart_data.ShoppingCartRepository
import com.example.catify.network.cart_data.ShoppingCartSerializer


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(layoutInflater, container, false)
        shoppingCartRepository = ShoppingCartRepository(requireContext().shoppingCartDataStore)
        shoppingCartListAdapter = ShoppingCartListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingCartViewModel = ViewModelProvider(
            this,
            ShoppingCartViewModel.provideFactory((shoppingCartRepository))
        )[ShoppingCartViewModel::class.java]
        applyBindings()
        applyObservers()
        shoppingCartListAdapter.submitList(shoppingCartViewModel.tempCartItems)
    }

    private fun applyObservers() {
        shoppingCartViewModel.shoppingCart.observe(viewLifecycleOwner) {
            Log.d(TAG, "Shopping Cart: ${it.cartItemListList.size}")
            shoppingCartViewModel.tempCartItems = it.cartItemListList
            shoppingCartListAdapter.submitList(it.cartItemListList)
        }
    }

    private fun applyBindings() {
        binding.shoppingListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.shoppingListRecyclerView.adapter = shoppingCartListAdapter
        binding.addItemTextButton.setOnClickListener {
            shoppingCartViewModel.addCartItem(
                item = CartItem.getDefaultInstance()
            ) }
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }
}