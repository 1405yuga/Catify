package com.example.homecatlog.helper

import androidx.room.TypeConverter
import com.example.homecatlog.entity.Catalog
import com.example.homecatlog.entity.HomeItem
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(homeItems: List<HomeItem>): String = Gson().toJson(homeItems)

    @TypeConverter
    fun jsonToList(value: String): List<HomeItem> =
        Gson().fromJson(value, Array<HomeItem>::class.java).toList()

    fun catalogToJson(catalog: Catalog): String = Gson().toJson(catalog)

    fun jsonToCatalog(value: String): Catalog = Gson().fromJson(value, Catalog::class.java)
}