package com.example.catify.ui.edit_catlog_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.catify.entity.Catalog
import com.example.catify.entity.CatalogListItem

class UpdateCatalogViewModel : ViewModel() {
    private var catalog: Catalog? = null
    private val TAG = this.javaClass.simpleName

    fun initialise(catalog: Catalog) {
        this.catalog = catalog
    }

    fun getCatalog(): Catalog {
        return this.catalog!!
    }

    fun reAddHomeItem(pos: Int, catalogListItem: CatalogListItem): Catalog {
        val updatedHomeItemsList =
            this.catalog?.catalogListItems?.toMutableList()?.apply { add(pos, catalogListItem) }
        catalog = updatedHomeItemsList?.let { catalog?.copy(catalogListItems = it) }
        return catalog ?: Catalog(
            category = "Untitled",
            catalogListItems = mutableListOf(CatalogListItem("Untitled", 0))
        )
    }

    fun removeHomeItem(pos: Int): Catalog {
        val updatedHomeItemsList = this.catalog?.catalogListItems?.toMutableList()?.apply { removeAt(pos) }
        catalog = updatedHomeItemsList?.let { catalog?.copy(catalogListItems = it) }
        return catalog ?: Catalog(
            category = "Untitled",
            catalogListItems = mutableListOf(CatalogListItem("Untitled", 0))
        )
    }

    fun removeEmptyHomeItem(): List<CatalogListItem> {
        val updatedHomeItemsList = this.catalog?.catalogListItems?.toMutableList()
        updatedHomeItemsList?.removeAll { item -> item.itemName.trim().isBlank() }
        catalog = updatedHomeItemsList?.let { catalog?.copy(catalogListItems = it) }
        return catalog?.catalogListItems ?: mutableListOf(CatalogListItem("Untitled", 0))
    }

    fun addHomeItemView(position: Int, remainingText: String): Catalog {
        val updatedHomeItems = this.catalog?.catalogListItems?.toMutableList()
        if (updatedHomeItems?.contains(CatalogListItem("", 0)) == false) {
            updatedHomeItems.add(index = position, element = CatalogListItem(remainingText, 0))
        }
        catalog = updatedHomeItems?.let { catalog?.copy(catalogListItems = it) }
        return catalog ?: Catalog(
            category = "",
            catalogListItems = mutableListOf(CatalogListItem("", 0))
        )
    }

    fun moveToPrevTextField(prevPosition: Int, remainingText: String): Catalog {
        val updatedHomeItemsList = this.catalog?.catalogListItems?.toMutableList()
        val oldHomeItem = updatedHomeItemsList?.get(prevPosition)
        updatedHomeItemsList!![prevPosition] = CatalogListItem(
            oldHomeItem!!.itemName + remainingText,
            oldHomeItem.availableStock
        )
        catalog = updatedHomeItemsList.let { catalog?.copy(catalogListItems = it) }
        return catalog ?: Catalog(
            category = "",
            catalogListItems = mutableListOf(CatalogListItem("", 0))
        )
    }
}