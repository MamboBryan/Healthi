package com.mambo.template.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val preference: SharedPreferences
) {

    companion object {
        const val TARGET_CALORIES = "target_calories"
    }

    fun getTargetCalories() = preference
        .getInt(TARGET_CALORIES, -1)

    fun setCalories(value: Int) {
        val editor = preference.edit()
        editor.putInt(TARGET_CALORIES, value)
        editor.apply()
    }
}