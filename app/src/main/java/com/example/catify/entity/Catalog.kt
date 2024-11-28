package com.example.catify.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Catalog(
    @PrimaryKey(autoGenerate = true)
    val catalogId: Int = 0,
    var category: String,
    var catalogListItems: MutableList<CatalogListItem>
) {
    fun updateCategory(newCategory: String) {
        this.category = newCategory
    }

    fun deleteWithTransferAndReturnCursorIndex(index: Int): Int? {
        try {
            val cursorIndex: Int?
            if (index > 0) {
                val toDelete = this.catalogListItems.getOrNull(index = index)
                val destination = this.catalogListItems.getOrNull(index = index - 1)
                cursorIndex = destination?.itemName?.length
                destination?.itemName += toDelete?.itemName ?: ""
            } else {
                cursorIndex = null
            }
            this.catalogListItems.removeAt(index = index)
            return cursorIndex
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
