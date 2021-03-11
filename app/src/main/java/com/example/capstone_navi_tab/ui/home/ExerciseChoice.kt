package com.example.capstone_navi_tab.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone_navi_tab.R
import com.example.capstone_navi_tab.databinding.FragmentChoiceBinding
import com.example.capstone_navi_tab.databinding.FragmentSettingsBinding

class ExerciseChoice : Fragment() {

    lateinit var binding: FragmentChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoiceBinding.inflate(layoutInflater)
        return binding.root
    }

}