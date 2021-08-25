package com.mambo.template.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mambo.template.data.model.FoodItem

class Converters {

    @TypeConverter
    fun fromFoodsList(foods: List<FoodItem>): String {
        return Gson().toJson(foods)
    }

    @TypeConverter
    fun toFoodsList(json: String): List<FoodItem> {
        val myType = object : TypeToken<List<FoodItem>>() {}.type
        return Gson().fromJson(json, myType)
    }

}