package com.mambo.template.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(
    val id: String,
    val name: String,
    val image: String,
    var user_servings: Double,
    val serving_quantity: Double,
    val calories_per_serving: Double
) : Parcelable {

    val total_calories = (user_servings * calories_per_serving) / serving_quantity

}