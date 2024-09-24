package com.example.homecatlog.ui.edit_catlog_fragment

import androidx.lifecycle.ViewModel
import com.example.homecatlog.entity.Catalog

class UpdateCatalogViewModel : ViewModel() {
    private var catalog: Catalog? = null

    fun initialise(catalog: Catalog) {
        if (this.catalog == null) this.catalog = catalog
    }

    fun increaseQuantity(itemName: String): Int {
        this.catalog?.homeItems?.find { homeItem -> homeItem.itemName == itemName }?.let { item ->
            item.availableStock += 1
        }
        return this.catalog?.homeItems?.find { homeItem -> homeItem.itemName == itemName }?.availableStock
            ?: 0
    }
}