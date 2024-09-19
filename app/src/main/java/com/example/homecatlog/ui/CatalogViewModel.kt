package com.example.homecatlog.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.entity.HomeItem
import com.example.homecatlog.network.CategoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogViewModel(private val categoryDao: CategoryDao) : ViewModel() {

    val allCatalogs: LiveData<List<Catalog>> = categoryDao.getAllCatalogs().asLiveData()
    private val TAG = this.javaClass.simpleName

    fun addCatalog(
        category: String,
        homeItems: List<HomeItem>,
        onSuccess: (String) -> (Unit),
        onFailure: (String) -> (Unit)
    ) {
        val catalog = Catalog(category, homeItems)
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val rowId = categoryDao.addCatalog(catalog)
                if (rowId != -1L) {
                    Log.d(TAG, "Added catalog successfully")
                    withContext(Dispatchers.Main) { onSuccess(catalog.category) }
                } else {
                    Log.e(TAG, "Failed to add catalog")
                    withContext(Dispatchers.Main) { onFailure("Failed to add catalog") }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while adding catalog :" + e.message)
                withContext(Dispatchers.Main) { onFailure("Error while adding catalog") }
            }

        }
    }

    class CatalogViewModelFactory(private val categoryDao: CategoryDao) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatalogViewModel(categoryDao) as T
            }
            throw IllegalArgumentException("uknown viewmodel")
        }
    }
}