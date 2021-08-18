package com.mambo.template.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val preference: SharedPreferences
) {

    companion object {
        const val TARGET_CALORIES = "target_calories"
        const val DARK_THEME = "dark_theme"
    }

    fun getTargetCalories() = preference
        .getInt(TARGET_CALORIES, -1)

    fun setCalories(value: Int) {
        val editor = preference.edit()
        editor.putInt(TARGET_CALORIES, value)
        editor.apply()
    }

    fun isDarkThemeEnabled() = preference
        .getBoolean(DARK_THEME, true)

    fun setDarkThemeEnabled(value: Boolean) {
        val editor = preference.edit()
        editor.putBoolean(DARK_THEME, value)
        editor.apply()
    }
}