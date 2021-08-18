package com.mambo.template.ui.home

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

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvHomeDay.text = "${viewModel.day.value}"
            tvHomeDate.text = "${viewModel.date.value}"

            ivHomeSettings.setOnClickListener {
                viewModel.navigateToSettings()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    HomeViewModel.HomeEvent.NavigateToSettings -> {
                        findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    }
                }
            }
        }
    }
}