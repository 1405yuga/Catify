package com.example.homecatlog.ui

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

class CatalogViewModel(private val categoryDao: CategoryDao) : ViewModel() {

    val allCatalogs: LiveData<List<Catalog>> = categoryDao.getAllCatalogs().asLiveData()

    fun addCatalog(category: String, homeItems: List<HomeItem>) {
        val catalog = Catalog(category, homeItems)
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.addCatalog(catalog)
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