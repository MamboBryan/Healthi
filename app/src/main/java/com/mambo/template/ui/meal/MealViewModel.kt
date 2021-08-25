package com.mambo.template.ui.meal

import androidx.lifecycle.*
import com.mambo.template.data.local.MealsDao
import com.mambo.template.data.model.FoodItem
import com.mambo.template.data.model.Meal
import com.mambo.template.data.model.response.SearchResponse
import com.mambo.template.data.repositories.NutritionixRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MealViewModel @Inject constructor(
    state: SavedStateHandle,
    private val mealsDao: MealsDao,
    private val repository: NutritionixRepository
) : ViewModel() {

    private val _editMealEventChannel = Channel<EditMealEvent>()
    val editMealEvent = _editMealEventChannel.receiveAsFlow()

    val meal = state.get<Meal>("meal")

    var mealName = meal?.name ?: ""
    var mealDate = MutableLiveData(meal?.date ?: Calendar.getInstance().timeInMillis)
    var mealTime = MutableLiveData(meal?.time ?: Calendar.getInstance().time.time)
    var mealFoods = MutableLiveData<List<FoodItem>>()

    val title = if (meal == null) "Add Meal" else "Edit Meal"
    val isDeleteEnabled = meal != null

    private val foodQuery = MutableLiveData("")
    val searchedFoods = MutableLiveData<List<SearchResponse.Food>>()
    var selectedFood: FoodItem? = null

    init {
        if (meal != null)
            mealFoods.value = meal.foods
    }

    fun getDate(): String {

        val sdf = SimpleDateFormat("MMM dd, yyyy")
        return sdf.format(mealDate.value)

    }

    fun getTime(): String {

        val sdf = SimpleDateFormat("h:mm a")
        return sdf.format(mealTime.value)

    }

    private fun getMealDay(): String {

        val sdf = SimpleDateFormat("yyMMdd")
        return sdf.format(mealDate.value)

    }

    fun onSaveMealClicked() {

        when {

            mealName.isBlank() -> {
                sendEventMessage("Meal Name Cannot be Blank")
            }

            mealFoods.value.isNullOrEmpty() -> {
                sendEventMessage("Food Items cannot be empty")
            }

            else -> {
                if (meal == null) {
                    saveMeal()
                } else {
                    updateMeal()
                }
            }

        }

    }

    fun onDeleteMealClicked() {}

    fun onDateSelected(date: Calendar) {
        mealDate.value = date.timeInMillis
    }

    fun onTimeSelected(date: Calendar) {
        mealTime.value = date.time.time
    }

    fun onSearchQueryAdded(query: String) {
        searchFoodItemsByName(query)
    }

    fun onSearchFoodSelected(foodItem: FoodItem) {
        selectedFood = foodItem

        viewModelScope.launch {
            _editMealEventChannel.send(EditMealEvent.NavigateToEditFoodServings)
        }
    }

    fun onFoodItemDeleted(foodItem: FoodItem) {

        val foods = mealFoods.value.orEmpty().toMutableList()

        if (foods.contains(foodItem))
            foods.remove(foodItem)
        mealFoods.value = foods

        sendEventMessage("Food Item Deleted")
    }

    private fun onFoodItemAdded(foodItem: FoodItem) {

        val foods = mealFoods.value.orEmpty().toMutableList()

        if (foods.contains(selectedFood))
            foods.remove(selectedFood)
        foods.add(foodItem)

        selectedFood = null
        mealFoods.value = foods

        sendEventMessage("Food Item Added")
    }

    private fun sendEventMessage(message: String) {
        viewModelScope.launch {
            _editMealEventChannel.send(EditMealEvent.ShowActionMessage(message))
        }
    }

    fun onSelectedFoodUpdated(servings: Double) {
        val updatedFood = selectedFood!!
        updatedFood.user_servings = servings

        onFoodItemAdded(updatedFood)
    }

    private fun saveMeal() {

        val meal = Meal(
            name = mealName,
            date = mealDate.value!!,
            day = getMealDay(),
            time = mealTime.value!!,
            foods = mealFoods.value!!
        )

        viewModelScope.launch {
            mealsDao.insert(meal)
            _editMealEventChannel.send(EditMealEvent.NavigateToHome("Meal Added"))
        }

    }

    private fun updateMeal() {

        val newMeal = meal?.copy(
            name = mealName,
            date = mealDate.value!!,
            day = getMealDay(),
            time = mealTime.value!!,
            foods = mealFoods.value!!
        )!!

        viewModelScope.launch {
            mealsDao.insert(newMeal)
            _editMealEventChannel.send(EditMealEvent.NavigateToHome("Meal Updated"))
        }

    }

    private fun searchFoodItemsByName(query: String) {
        viewModelScope.launch {
            val response = repository.getSearchedFoods(query)
            foodQuery.value = query
            searchedFoods.value = response.branded
        }
    }


    sealed class EditMealEvent {
        object NavigateToEditFoodServings : EditMealEvent()
        data class NavigateToHome(val message: String) : EditMealEvent()
        data class ShowActionMessage(val message: String) : EditMealEvent()
    }

}