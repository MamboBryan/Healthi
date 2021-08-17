package com.mambo.template.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/instant")
    suspend fun getSearchedFood(
        @Query("query") query: String
    )
}
