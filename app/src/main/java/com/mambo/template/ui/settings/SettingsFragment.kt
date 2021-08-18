package com.mambo.template.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.mambo.template.R
import com.mambo.template.databinding.FragmentSettingsBinding
import com.mambo.template.databinding.FragmentSetupBinding
import com.mambo.template.ui.main.MainActivity
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            NavigationUI.setupWithNavController(toolbar, findNavController())

            viewModel.isDarkThemeEnabled.observe(viewLifecycleOwner, { isEnabled ->
                switchDarkmode.isChecked = isEnabled
            })

            viewModel.targetCalories.observe(viewLifecycleOwner, { calories ->
                tvSettingsCalories.text = "$calories kcal"
            })

            switchDarkmode.setOnCheckedChangeListener { _, isEnabled ->
                viewModel.onDarkThemeSwitchClicked(isEnabled)
            }

            edtSettingsCalories.addTextChangedListener(object : TextWatcher {
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

            btnSettingsUpdate.setOnClickListener { viewModel.onUpdateButtonClicked() }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.settingsEvent.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.ShowInvalidCaloriesMessage -> {
                        binding.apply { textInputLayout2.error = event.message }
                    }
                    is SettingsViewModel.SettingsEvent.ShowUpdatedCaloriesMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is SettingsViewModel.SettingsEvent.UpdateDarkTheme -> {
                        (activity as MainActivity?)?.updateTheme()
                    }
                }
            }
        }
    }

}