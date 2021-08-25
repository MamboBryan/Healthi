package com.mambo.template.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mambo.template.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.MEALS_TABLE_NAME)
@Parcelize
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: Long,
    val day: String,
    val time: Long,
    val foods: List<FoodItem>
) : Parcelable {

    fun totalCalories(): Double {
        return foods.map { foodItem -> foodItem.total_calories }
            .reduce { acc, calories -> acc.plus(calories) }
    }

}