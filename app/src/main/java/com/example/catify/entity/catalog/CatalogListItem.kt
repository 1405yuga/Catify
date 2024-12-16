package com.example.catify.entity.catalog

data class CatalogListItem(var itemName: String, var availableStock: Int) {
    fun decreaseQty() {
        if (availableStock > 0) {
            availableStock--
        }
    }
}
