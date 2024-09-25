package com.example.homecatlog.ui.edit_catlog_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.homecatlog.entity.Catalog

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
            item.availableStock += 1
            item.availableStock
        }
    }

    fun decreaseQuantity(itemName: String): Int {
        Log.d(TAG, "Catalog in view model $catalog")
        val item = this.catalog?.homeItems?.find { homeItem -> homeItem.itemName == itemName }
        return if (item == null) 0
        else {
            item.availableStock -= 1
            item.availableStock
        }
    }
}