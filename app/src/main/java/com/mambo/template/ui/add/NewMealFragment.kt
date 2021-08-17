package com.mambo.template.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mambo.template.R
import com.mambo.template.databinding.FragmentNewMealBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMealFragment : Fragment(R.layout.fragment_new_meal) {

    private val binding by viewBinding(FragmentNewMealBinding::bind)

}