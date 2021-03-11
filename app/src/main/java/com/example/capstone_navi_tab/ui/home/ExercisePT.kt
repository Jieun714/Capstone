package com.example.capstone_navi_tab.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone_navi_tab.R
import com.example.capstone_navi_tab.databinding.FragmentChoiceBinding
import com.example.capstone_navi_tab.databinding.FragmentPtBinding

class ExercisePT : Fragment() {

    lateinit var binding: FragmentPtBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPtBinding.inflate(layoutInflater)
        return binding.root
    }
}