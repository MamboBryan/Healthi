package com.mambo.template.ui.setup

import android.app.Application
import androidx.lifecycle.*
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
) : ViewModel() {

    val targetCalories: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    private val _setupEventChannel = Channel<SetupEvent>()
    val setupEvent = _setupEventChannel.receiveAsFlow()

    fun onStartButtonClicked() {

        when {
            targetCalories.value!! <= 0 -> {
                showInvalidCalories("Calories cannot be 0 or below")
            }
            else -> {
                saveTargetCalories()
            }
        }

    }

    fun updateTargetCalories(targetInput: Int) {
        targetCalories.value = targetInput
    }

    private fun showInvalidCalories(message: String) {
        viewModelScope.launch {
            _setupEventChannel.send(SetupEvent.ShowInvalidCaloriesMessage(message))
        }
    }

    private fun saveTargetCalories() {
        preferenceManager.setCalories(targetCalories.value!!)
        viewModelScope.launch {
            _setupEventChannel.send(SetupEvent.NavigateToHome)
        }
    }

    sealed class SetupEvent {
        data class ShowInvalidCaloriesMessage(val message: String) : SetupEvent()
        object NavigateToHome : SetupEvent()
    }

}