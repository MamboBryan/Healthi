package com.mambo.template.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _homeEventChannel = Channel<HomeEvent>()
    val homeEvent = _homeEventChannel.receiveAsFlow()

    val targetCalories = preferenceManager.getTargetCalories()
    val eatenCalories = MutableLiveData(300)

    private val today = Calendar.getInstance()
    val datePicked = MutableLiveData(today)

    val day = MutableLiveData("Thursday")
    val date = MutableLiveData("")

    init {
        getDate()
    }

    fun getDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy")

        return sdf.format(datePicked.value?.time)

    }

    fun getDay(): String {
        val sdf = SimpleDateFormat("EEEE")
        val comparisonFormat = SimpleDateFormat("dd/MM/yyyy")

        return when {
            comparisonFormat.format(datePicked.value?.time) != comparisonFormat.format(today.time)
            -> sdf.format(datePicked.value?.time)
            else -> "Today"
        }

    }

    fun onSettingsClicked() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToSettings)
        }
    }

    fun onDateSelected(calendar: Calendar) {
        datePicked.value = calendar
    }

    fun onDayClicked() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.OpenCalendar)
        }
    }

    fun onAddNewMealClicked(){
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToAddNewMeal)
        }
    }

    sealed class HomeEvent {
        object NavigateToSettings : HomeEvent()
        object NavigateToAddNewMeal : HomeEvent()
        object OpenCalendar : HomeEvent()
    }

}