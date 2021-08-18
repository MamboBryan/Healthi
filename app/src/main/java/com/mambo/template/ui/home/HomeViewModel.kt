package com.mambo.template.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mambo.template.ui.setup.SetupViewModel
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _homeEventChannel = Channel<HomeEvent>()
    val homeEvent = _homeEventChannel.receiveAsFlow()

    val targetCalories = preferenceManager.getTargetCalories()

    val day = MutableLiveData("Thursday")
    val date = MutableLiveData("")

    init {
        getDate()
    }

    private fun getDate() {
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        val currentDate = sdf.format(Date())

        date.value = currentDate

    }

    fun navigateToSettings() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToSettings)
        }
    }

    sealed class HomeEvent {
        object NavigateToSettings : HomeEvent()
    }

}