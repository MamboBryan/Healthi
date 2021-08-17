package com.mambo.template.ui.home

import androidx.fragment.app.Fragment
import com.mambo.template.R
import com.mambo.template.databinding.FragmentMainBinding
import com.mambo.template.databinding.FragmentNewMealBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

}