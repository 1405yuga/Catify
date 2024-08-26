package com.example.homecatlog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.repository.CatalogDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogViewModel(private val catalogDao: CatalogDao) : ViewModel() {

    val catlogs: LiveData<List<Catalog>> = catalogDao.getAllCatlogs().asLiveData()

    public fun retrieveCatlog(category: String): LiveData<Catalog> {
        return catalogDao.getCatlogByCategory(category).asLiveData()
    }

    public fun insertCatlog(catalog: Catalog) {
        viewModelScope.launch(Dispatchers.IO) {
            catalogDao.addCatlog(catalog)
        }
    }
}