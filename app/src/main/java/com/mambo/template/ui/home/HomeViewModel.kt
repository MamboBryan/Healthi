package com.mambo.template.ui.home

import androidx.lifecycle.*
import com.mambo.template.data.local.MealsDao
import com.mambo.template.data.model.Meal
import com.mambo.template.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferenceManager: PreferenceManager,
    private val mealsDao: MealsDao
) : ViewModel() {

    private val _homeEventChannel = Channel<HomeEvent>()
    val homeEvent = _homeEventChannel.receiveAsFlow()

    private val query = MutableStateFlow("")

    private val _meals = query.flatMapLatest { mealsDao.getPoemsByDate(it) }
    val meals = _meals.asLiveData()

    val targetCalories = preferenceManager.getTargetCalories()
    var eatenCalories = MutableLiveData(0)

    private val today = Calendar.getInstance()
    val datePicked = MutableLiveData(today)

    init {
        getDate()
        updateDayName()
    }

    fun getDate(): String {

        val sdf = SimpleDateFormat("MMM dd, yyyy")
        return sdf.format(datePicked.value?.time)

    }

    fun getWeekdayName(): String {

        val sdf = SimpleDateFormat("EEEE")
        val comparisonFormat = SimpleDateFormat("dd/MM/yyyy")

        return when {
            comparisonFormat.format(datePicked.value?.time) != comparisonFormat.format(today.time)
            -> sdf.format(datePicked.value?.time)
            else -> "Today"
        }

    }

    private fun updateDayName() {
        val sdf = SimpleDateFormat("yyMMdd")
        query.value = sdf.format(datePicked.value?.timeInMillis)
    }

    fun onSettingsClicked() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToSettings)
        }
    }

    fun onDateSelected(calendar: Calendar) {
        datePicked.value = calendar
        updateDayName()
    }

    fun onDayClicked() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.OpenCalendar)
        }
    }

    fun onAddNewMealClicked() {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToAddNewMeal)
        }
    }

    fun onEditMealClicked(meal: Meal) {
        viewModelScope.launch {
            _homeEventChannel.send(HomeEvent.NavigateToEditMeal(meal))
        }
    }

    fun onDeleteMealClicked(meal: Meal) {
        viewModelScope.launch {
            mealsDao.delete(meal)
            _homeEventChannel.send(HomeEvent.ShowConfirmationMessage("Meal Deleted"))
        }
    }

    sealed class HomeEvent {
        object NavigateToSettings : HomeEvent()
        object NavigateToAddNewMeal : HomeEvent()
        data class NavigateToEditMeal(val meal: Meal) : HomeEvent()
        object OpenCalendar : HomeEvent()
        data class ShowConfirmationMessage(val message: String) : HomeEvent()
    }

}