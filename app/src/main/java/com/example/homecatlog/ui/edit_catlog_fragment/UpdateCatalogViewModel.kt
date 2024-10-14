package com.example.homecatlog.ui.edit_catlog_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.entity.HomeItem

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

    fun addHomeItemView(): Catalog {
        val updatedHomeItems = this.catalog?.homeItems?.toMutableList()
        if (updatedHomeItems != null && !updatedHomeItems.any { homeItem -> homeItem.itemName == "Untitled" }) updatedHomeItems?.add(
            HomeItem("Untitled", 0)
        )
        catalog = updatedHomeItems?.let { catalog?.copy(homeItems = it) }
        return catalog ?: Catalog("Untitled", homeItems = mutableListOf(HomeItem("Untitled", 0)))
    }
}