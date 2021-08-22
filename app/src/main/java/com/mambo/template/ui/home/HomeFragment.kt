package com.mambo.template.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mambo.template.R
import com.mambo.template.databinding.FragmentHomeBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.datePicked.observe(viewLifecycleOwner) {
                tvHomeDay.text = viewModel.getDay()
                tvHomeDate.text = viewModel.getDate()
            }

            viewModel.eatenCalories.observe(viewLifecycleOwner) { eatenCalories ->
                val caloriesLeft = viewModel.targetCalories - eatenCalories

                tvCaloriesEaten.text = "$eatenCalories"
                tvCaloriesLeft.text = "$caloriesLeft kcal left"
            }

            layoutDate.setOnClickListener { viewModel.onDayClicked() }
            ivHomeSettings.setOnClickListener { viewModel.onSettingsClicked() }
            fabAddMeal.setOnClickListener { viewModel.onAddNewMealClicked() }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    HomeViewModel.HomeEvent.NavigateToSettings -> {
                        findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    }
                    HomeViewModel.HomeEvent.OpenCalendar -> {
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
                    HomeViewModel.HomeEvent.NavigateToAddNewMeal -> {
                        findNavController().navigate(R.id.action_mainFragment_to_newMealFragment)
                    }
                }
            }
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()

            newDate.set(Calendar.YEAR, year)
            newDate.set(Calendar.MONTH, monthOfYear)
            newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            viewModel.onDateSelected(newDate)
        }
}