package com.mambo.template.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mambo.template.databinding.LayoutBottomSheetFoodBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class EditFoodBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditFoodBottomSheet"
    }

    private var _binding: LayoutBottomSheetFoodBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MealViewModel>({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val food = viewModel.selectedFood!!

        binding.apply {

            Glide
                .with(ivSheetFoodImage)
                .load(food.image)
                .into(ivSheetFoodImage)

            tvSheetFoodName.text = food.name
            tvSheetServingsCalories.text =
                "${food.calories_per_serving} kcal per ${food.serving_quantity} serving"

            btnSave.setOnClickListener {
                viewModel.onSelectedFoodUpdated(edtServings.text.toString().toDouble())
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}