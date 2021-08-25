package com.mambo.template.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mambo.template.R
import com.mambo.template.data.model.Meal
import com.mambo.template.databinding.FragmentHomeBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), MealAdapter.OnMealItemClickListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = MealAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            layoutMeals.tvEmpty.text = "No Meals Added"
            layoutMeals.recyclerView.adapter = adapter

            viewModel.datePicked.observe(viewLifecycleOwner) {
                tvHomeDay.text = viewModel.getWeekdayName()
                tvHomeDate.text = viewModel.getDate()
            }

            viewModel.eatenCalories.observe(viewLifecycleOwner) { eatenCalories ->
                val caloriesLeft = viewModel.targetCalories - eatenCalories

                tvCaloriesEaten.text = "$eatenCalories"
                tvCaloriesLeft.text = "$caloriesLeft kcal left"

                progressBar.max = viewModel.targetCalories
                progressBar.progress = eatenCalories
            }

            viewModel.meals.observe(viewLifecycleOwner) { meals ->
                layoutMeals.stateLoading.isVisible = meals == null
                layoutMeals.stateEmpty.isVisible = meals.isEmpty()
            }

            layoutDate.setOnClickListener { viewModel.onDayClicked() }
            ivHomeSettings.setOnClickListener { viewModel.onSettingsClicked() }
            fabAddMeal.setOnClickListener { viewModel.onAddNewMealClicked() }

        }

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    HomeViewModel.HomeEvent.NavigateToSettings -> {
                        findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    }
                    HomeViewModel.HomeEvent.OpenCalendar -> {
                        openDateDialog()

                    }
                    HomeViewModel.HomeEvent.NavigateToAddNewMeal -> {
                        findNavController().navigate(R.id.action_mainFragment_to_mealFragment)
                    }
                    is HomeViewModel.HomeEvent.NavigateToEditMeal -> {
                        val action =
                            HomeFragmentDirections.actionMainFragmentToMealFragment(event.meal)
                        findNavController().navigate(action)
                    }
                    is HomeViewModel.HomeEvent.ShowConfirmationMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.apply {

                val eatenCalories =
                    if (meals.isNullOrEmpty()) 0 else meals.map { meal -> meal.totalCalories() }
                        .reduce { acc, calories -> acc.plus(calories) }

                viewModel.eatenCalories.value = eatenCalories.toInt()

                adapter.submitList(meals)

                layoutMeals.stateEmpty.isVisible = meals.isNullOrEmpty()
                layoutMeals.stateContent.isVisible = !meals.isNullOrEmpty()

            }

        }
    }

    private fun openDateDialog() {
        val datePicked = viewModel.datePicked.value!!
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            datePicked.get(Calendar.YEAR),
            datePicked.get(Calendar.MONTH),
            datePicked.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()

            newDate.set(Calendar.YEAR, year)
            newDate.set(Calendar.MONTH, monthOfYear)
            newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            viewModel.onDateSelected(newDate)
        }

    override fun onMealItemEditClicked(meal: Meal) {
        viewModel.onEditMealClicked(meal)
    }

    override fun onMealItemDeleteClicked(meal: Meal) {
        viewModel.onDeleteMealClicked(meal)
    }
}