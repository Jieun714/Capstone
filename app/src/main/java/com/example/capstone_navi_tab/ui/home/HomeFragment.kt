package com.example.capstone_navi_tab.ui.home

import android.app.AppComponentFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone_navi_tab.R
import com.example.capstone_navi_tab.databinding.FragmentHomeBinding

class HomeFragment : Fragment(){

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }
}