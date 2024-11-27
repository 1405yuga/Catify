package com.example.catify.ui.edit_catlog_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.catify.entity.Catalog
import com.example.catify.entity.HomeItem

class UpdateCatalogViewModel : ViewModel() {
    private var catalog: Catalog? = null
    private val TAG = this.javaClass.simpleName

    fun initialise(catalog: Catalog) {
        this.catalog = catalog
    }

    fun increaseQuantity(itemName: String): Int {
        Log.d(TAG, "Catalog in view model $catalog")
        val item = this.catalog?.homeItems?.find { homeItem -> homeItem.itemName == itemName }
        return if (item == null) 0
        else {
            if (item.availableStock < Int.MAX_VALUE) item.availableStock += 1
            item.availableStock
        }
    }

    fun decreaseQuantity(itemName: String): Int {
        Log.d(TAG, "Catalog in view model $catalog")
        val item = this.catalog?.homeItems?.find { homeItem -> homeItem.itemName == itemName }
        return if (item == null || item.availableStock == 0) 0
        else {
            item.availableStock -= 1
            item.availableStock
        }
    }

    fun reAddHomeItem(pos: Int, homeItem: HomeItem): Catalog {
        val updatedHomeItemsList =
            this.catalog?.homeItems?.toMutableList()?.apply { add(pos, homeItem) }
        catalog = updatedHomeItemsList?.let { catalog?.copy(homeItems = it) }
        return catalog ?: Catalog(
            category = "Untitled",
            homeItems = mutableListOf(HomeItem("Untitled", 0))
        )
    }

    fun removeHomeItem(pos: Int): Catalog {
        val updatedHomeItemsList = this.catalog?.homeItems?.toMutableList()?.apply { removeAt(pos) }
        catalog = updatedHomeItemsList?.let { catalog?.copy(homeItems = it) }
        return catalog ?: Catalog(
            category = "Untitled",
            homeItems = mutableListOf(HomeItem("Untitled", 0))
        )
    }

    fun removeEmptyHomeItem(): List<HomeItem> {
        val updatedHomeItemsList = this.catalog?.homeItems?.toMutableList()
        updatedHomeItemsList?.removeAll { item -> item.itemName.trim().isBlank() }
        catalog = updatedHomeItemsList?.let { catalog?.copy(homeItems = it) }
        return catalog?.homeItems ?: mutableListOf(HomeItem("Untitled", 0))
    }

    fun addHomeItemView(position: Int, remainingText: String): Catalog {
        val updatedHomeItems = this.catalog?.homeItems?.toMutableList()
        if (updatedHomeItems?.contains(HomeItem("", 0)) == false) {
            updatedHomeItems.add(index = position, element = HomeItem(remainingText, 0))
        }
        catalog = updatedHomeItems?.let { catalog?.copy(homeItems = it) }
        return catalog ?: Catalog(
            category = "",
            homeItems = mutableListOf(HomeItem("", 0))
        )
    }

    fun moveToPrevTextField(prevPosition: Int, remainingText: String): Catalog {
        val updatedHomeItemsList = this.catalog?.homeItems?.toMutableList()
        val oldHomeItem = updatedHomeItemsList?.get(prevPosition)
        updatedHomeItemsList!![prevPosition] = HomeItem(
            oldHomeItem!!.itemName + remainingText,
            oldHomeItem.availableStock
        )
        catalog = updatedHomeItemsList.let { catalog?.copy(homeItems = it) }
        this.catalog = removeHomeItem(prevPosition + 1)
        return catalog ?: Catalog(
            category = "",
            homeItems = mutableListOf(HomeItem("", 0))
        )
    }
}