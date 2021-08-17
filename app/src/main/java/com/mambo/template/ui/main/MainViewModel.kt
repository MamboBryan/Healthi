package com.mambo.template.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mambo.template.data.repositories.NutritionixRepository
import com.mambo.template.utils.ConnectionLiveData
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    preferenceManager: PreferenceManager,
    private val nutritionixRepository: NutritionixRepository
) : AndroidViewModel(application) {

    val targetCalories = preferenceManager.getTargetCalories()

    private val _connection = ConnectionLiveData(application)
    private val _settingsEventChannel = Channel<MainEvent>()

    var backIsPressedOnce = false

    val connection: ConnectionLiveData
        get() = _connection

    val settingsEvent = _settingsEventChannel.receiveAsFlow()

    init {
        loadSearchedFood()
    }

    private fun loadSearchedFood() {
        viewModelScope.launch {
            nutritionixRepository.getSearchedFoods("bananas")
        }
    }

    sealed class MainEvent {
        data class UpdateAppTheme(val isDarkModeEnable: Boolean) : MainEvent()
    }
}