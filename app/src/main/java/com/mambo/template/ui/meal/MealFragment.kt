package com.mambo.template.ui.meal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.mambo.template.R
import com.mambo.template.data.model.FoodItem
import com.mambo.template.databinding.FragmentMealBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class MealFragment : Fragment(R.layout.fragment_meal), FoodAdapter.OnFoodItemClickListener {

    private val binding by viewBinding(FragmentMealBinding::bind)
    private val viewModel by viewModels<MealViewModel>()
    private val adapter = FoodAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()

        setupAutoCompleteTextview()

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editMealEvent.collect { event ->
                when (event) {
                    MealViewModel.EditMealEvent.NavigateToEditFoodServings -> {
                        openEditServingBottomSheet()
                    }
                    is MealViewModel.EditMealEvent.ShowActionMessage -> {
                        showSnackBar(event.message)
                    }
                    is MealViewModel.EditMealEvent.NavigateToHome -> {
                        showSnackBar(event.message)
                        findNavController().popBackStack()
                    }
                }
            }
        }

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupUi() {
        binding.apply {

            NavigationUI.setupWithNavController(toolbarMeal, findNavController())
            toolbarMeal.title = viewModel.title
            toolbarMeal.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_meal_delete -> {
                        viewModel.onDeleteMealClicked()
                        true
                    }
                    R.id.action_meal_save -> {
                        viewModel.onSaveMealClicked()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            val deleteMenuItem = toolbarMeal.menu.findItem(R.id.action_meal_delete)
            deleteMenuItem.isVisible = viewModel.isDeleteEnabled

            edtMealName.setText(viewModel.mealName)
            edtMealName.doAfterTextChanged { name ->
                if (!name.isNullOrEmpty()) viewModel.mealName = name.toString()
            }

            edtMealDate.setOnClickListener { openDateDialog() }
            viewModel.mealDate.observe(viewLifecycleOwner) {
                edtMealDate.setText(viewModel.getDate())
            }

            edtMealTime.setOnClickListener { openTimeDialog() }
            viewModel.mealTime.observe(viewLifecycleOwner) {
                edtMealTime.setText(viewModel.getTime())
            }

            edtMealSearch.doAfterTextChanged { query ->
                if (!query.isNullOrEmpty() && query.length >= 3)
                    viewModel.onSearchQueryAdded(query.toString())
            }
            ivMealScan.setOnClickListener {
                IntentIntegrator.forSupportFragment(this@MealFragment).initiateScan();
            }

            layoutFoods.tvEmpty.text = "No Food added"
            layoutFoods.stateLoading.isVisible = false
            layoutFoods.stateEmpty.isVisible = viewModel.mealFoods.value.isNullOrEmpty()

        }
    }

    private fun setupAutoCompleteTextview() {
        viewModel.searchedFoods.observe(viewLifecycleOwner) { foods ->

            val strings: List<String> = foods.map { it.food_name }
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                strings
            )
            arrayAdapter.setNotifyOnChange(true)

            binding.apply {
                edtMealSearch.setAdapter(arrayAdapter)
                edtMealSearch.setOnItemClickListener { _, _, selectedPosition, id ->
                    val food = foods[selectedPosition]

                    val foodItem = FoodItem(
                        food.nix_item_id,
                        food.food_name,
                        food.photo.thumb,
                        food.serving_qty,
                        food.serving_qty,
                        food.nf_calories
                    )

                    viewModel.onSearchFoodSelected(foodItem)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        viewModel.mealFoods.observe(viewLifecycleOwner) { foods ->
            binding.apply {

                layoutFoods.recyclerView.adapter = adapter
                adapter.submitList(foods)

                layoutFoods.stateEmpty.isVisible = foods.isNullOrEmpty()
                layoutFoods.stateContent.isVisible = !foods.isNullOrEmpty()

            }

        }
    }

    private fun openTimeDialog() {
        val timePicked = Calendar.getInstance()
        timePicked.timeInMillis = viewModel.mealTime.value!!

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            timePicked.set(Calendar.HOUR_OF_DAY, hour)
            timePicked.set(Calendar.MINUTE, minute)

            viewModel.onTimeSelected(timePicked)
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            timePicked.get(Calendar.HOUR_OF_DAY),
            timePicked.get(Calendar.MINUTE),
            false
        ).show()

    }

    private fun openDateDialog() {

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()

                newDate.set(Calendar.YEAR, year)
                newDate.set(Calendar.MONTH, monthOfYear)
                newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                viewModel.onDateSelected(newDate)
            }

        val datePicked = Calendar.getInstance()
        datePicked.timeInMillis = viewModel.mealDate.value!!

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

    private fun openEditServingBottomSheet() {
        val bottomSheet = EditFoodBottomSheet()
        bottomSheet.showNow(childFragmentManager, EditFoodBottomSheet.TAG)
    }

    override fun onFoodItemEditClicked(foodItem: FoodItem) {
        viewModel.onSearchFoodSelected(foodItem)
    }

    override fun onFoodItemDeleteClicked(foodItem: FoodItem) {
        viewModel.onFoodItemDeleted(foodItem)
    }
}