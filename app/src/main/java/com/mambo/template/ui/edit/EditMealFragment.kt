package com.mambo.template.ui.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mambo.template.R
import com.mambo.template.databinding.FragmentEditMealBinding
import com.mambo.template.databinding.FragmentNewMealBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMealFragment : Fragment(R.layout.fragment_edit_meal) {

    private val binding by viewBinding(FragmentEditMealBinding::bind)

}