package com.example.homecatlog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.homecatlog.entity.Catlog
import com.example.homecatlog.repository.CatlogDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatlogViewModel(private val catlogDao: CatlogDao) : ViewModel() {

    val catlogs: LiveData<List<Catlog>> = catlogDao.getAllCatlogs().asLiveData()

    public fun retrieveCatlog(category: String): LiveData<Catlog> {
        return catlogDao.getCatlogByCategory(category).asLiveData()
    }

    public fun insertCatlog(catlog: Catlog) {
        viewModelScope.launch(Dispatchers.IO) {
            catlogDao.addCatlog(catlog)
        }
    }
}