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
import com.example.catify.ShoppingCartProto.ShoppingCart
import com.example.catify.databinding.FragmentShoppingCartBinding
import com.example.catify.network.cart_data.ShoppingCartRepository
import com.example.catify.network.cart_data.ShoppingCartSerializer


private val Context.shoppingCartDataStore: DataStore<ShoppingCart> by dataStore(
    fileName = "shopping_cart.pb",
    serializer = ShoppingCartSerializer
)

class ShoppingCartFragment : Fragment() {
    private final val TAG = this.javaClass.simpleName
    private lateinit var binding: FragmentShoppingCartBinding
    private lateinit var shoppingCartRepository: ShoppingCartRepository
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel

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

        applyBindings()
        applyObservers()
    }

    private fun applyObservers() {
        shoppingCartViewModel.shoppingCart.observe(viewLifecycleOwner) {
            Log.d(TAG, "Shopping Cart: $it")
        }
    }

    private fun applyBindings() {
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
    }
}