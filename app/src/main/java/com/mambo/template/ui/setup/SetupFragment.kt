package com.mambo.template.ui.setup

import androidx.fragment.app.Fragment
import com.mambo.template.R
import com.mambo.template.databinding.FragmentSetupBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private val binding by viewBinding(FragmentSetupBinding::bind)

}