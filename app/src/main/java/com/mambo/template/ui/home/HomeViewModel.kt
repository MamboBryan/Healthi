package com.mambo.template.ui.home

import androidx.lifecycle.ViewModel
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
) : ViewModel() {

    val targetCalories = preferenceManager.getTargetCalories()

}