package com.mambo.template.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchResponse(
    val branded: List<Food>
) {
    @Parcelize
    data class Food(
        val nix_item_id: String,
        val food_name: String,
        val serving_unit: String,
        val serving_qty: Double,
        val nf_calories: Double,
        val photo: FoodPhoto
    ) : Parcelable

    @Parcelize
    data class FoodPhoto(
        val thumb: String
    ) : Parcelable
}