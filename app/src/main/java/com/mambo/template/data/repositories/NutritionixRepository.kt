package com.mambo.template.data.repositories

import com.mambo.template.data.network.ApiService

class NutritionixRepository(
    private val apiService: ApiService
) {

    suspend fun getSearchedFoods(query: String) = apiService.getSearchedFood(query)

}