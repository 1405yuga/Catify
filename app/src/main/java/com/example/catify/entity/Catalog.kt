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

    fun addWithTransferAndReturn(index: Int, enterPressedPos: Int) {
        val current = this.catalogListItems.getOrNull(index)
        val firstPart = current?.itemName
            ?.slice(0 until enterPressedPos)
        val secondPart = current?.itemName
            ?.slice(enterPressedPos until current.itemName.length)
        if (firstPart != null) {
            current.itemName = firstPart
        }
        this.catalogListItems.add(
            index = index + 1,
            element = CatalogListItem(
                itemName = secondPart
                    ?: "",
                availableStock = 0
            )
        )
    }

    fun addBlankAtFirst() {
        this.catalogListItems.add(
            index = 0,
            element = CatalogListItem(itemName = "", availableStock = 0)
        )
    }
}
