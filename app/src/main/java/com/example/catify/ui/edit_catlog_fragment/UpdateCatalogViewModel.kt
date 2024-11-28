package com.example.catify.ui.edit_catlog_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.catify.entity.Catalog
import com.example.catify.entity.CatalogListItem

class UpdateCatalogViewModel : ViewModel() {
    private var _catalog: Catalog? = null
    val catalog: Catalog? get() = _catalog

    private val TAG = this.javaClass.simpleName

    fun initialise(catalog: Catalog) {
        if (this._catalog == null) {
            this._catalog = catalog
        } else {
            Log.d(TAG, "Already assigned")
        }
    }

    fun reAddHomeItem(pos: Int, catalogListItem: CatalogListItem): Catalog {
        val updatedHomeItemsList =
            this._catalog?.catalogListItems?.toMutableList()?.apply { add(pos, catalogListItem) }
        _catalog = updatedHomeItemsList?.let { _catalog?.copy(catalogListItems = it) }
        return _catalog ?: Catalog(
            category = "Untitled",
            catalogListItems = mutableListOf(CatalogListItem("Untitled", 0))
        )
    }

    fun addHomeItemView(position: Int, remainingText: String): Catalog {
        val updatedHomeItems = this._catalog?.catalogListItems?.toMutableList()
        if (updatedHomeItems?.contains(CatalogListItem("", 0)) == false) {
            updatedHomeItems.add(index = position, element = CatalogListItem(remainingText, 0))
        }
        _catalog = updatedHomeItems?.let { _catalog?.copy(catalogListItems = it) }
        return _catalog ?: Catalog(
            category = "",
            catalogListItems = mutableListOf(CatalogListItem("", 0))
        )
    }
}