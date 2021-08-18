package com.mambo.template.ui.setup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mambo.template.R
import com.mambo.template.databinding.FragmentSetupBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private val binding by viewBinding(FragmentSetupBinding::bind)
    private val viewModel by viewModels<SetupViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.targetCalories.observe(viewLifecycleOwner, { target ->
                tvSetupCalories.text = "Target : $target kcal"

                if (target > 0) {
                    textInputLayout.error = null
                }
            })

            edtCalories.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrBlank()) {
                        viewModel.updateTargetCalories(s.toString().toInt())
                    }
                }
            })

            btnSetupContinue.setOnClickListener { viewModel.onStartButtonClicked() }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect { event ->
                when (event) {
                    SetupViewModel.SetupEvent.NavigateToHome -> {
                        findNavController().navigate(R.id.action_setupFragment_to_mainFragment)
                    }
                    is SetupViewModel.SetupEvent.ShowInvalidCaloriesMessage -> {
                        binding.textInputLayout.error = event.message
                    }
                }
            }
        }
    }

}