package com.mambo.template.ui.settings

import android.app.Application
import android.content.res.Resources
import android.graphics.Paint
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvent = _settingsEventChannel.receiveAsFlow()

    val isDarkThemeEnabled = MutableLiveData(preferenceManager.isDarkThemeEnabled())

    private val target = MutableLiveData(0)
    val targetCalories: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(preferenceManager.getTargetCalories())
    }

    fun onUpdateButtonClicked() {

        when {
            target.value!! <= 0 -> {
                showInvalidCalories("Calories cannot be 0 or below")
            }
            else -> {
                updateTargetCalories()
            }
        }

    }

    fun updateTargetCalories(targetInput: Int) {
        target.value = targetInput
    }

    fun onDarkThemeSwitchClicked(isEnabled: Boolean) {
        preferenceManager.setDarkThemeEnabled(isEnabled)

        viewModelScope.launch {
            _settingsEventChannel.send(SettingsEvent.UpdateDarkTheme)
        }
    }

    private fun showInvalidCalories(message: String) {
        viewModelScope.launch {
            _settingsEventChannel.send(SettingsEvent.ShowInvalidCaloriesMessage(message))
        }
    }

    private fun updateTargetCalories() {
        preferenceManager.setCalories(target.value!!)
        targetCalories.value = target.value
        viewModelScope.launch {
            _settingsEventChannel.send(
                SettingsEvent.ShowUpdatedCaloriesMessage("Target updated successfully")
            )
        }
    }

    sealed class SettingsEvent {
        object UpdateDarkTheme : SettingsEvent()
        data class ShowInvalidCaloriesMessage(val message: String) : SettingsEvent()
        data class ShowUpdatedCaloriesMessage(val message: String) : SettingsEvent()
    }
}