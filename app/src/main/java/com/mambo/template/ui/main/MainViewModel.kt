package com.mambo.template.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    val preferenceManager: PreferenceManager,
) : AndroidViewModel(application) {

    val targetCalories = preferenceManager.getTargetCalories()

    private val _connection = ConnectionLiveData(application)

    var backIsPressedOnce = false

    val connection: ConnectionLiveData
        get() = _connection

}