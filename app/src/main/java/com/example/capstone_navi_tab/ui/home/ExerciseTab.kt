package com.example.capstone_navi_tab.ui.home

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.capstone_navi_tab.R
import com.example.capstone_navi_tab.databinding.FragmentSettingsBinding
import com.example.capstone_navi_tab.databinding.HomeTabsBinding
import com.google.android.material.tabs.TabLayout

class ExerciseTab : Fragment() {
    private var adapter: TabAdapter? = null
    private var tableLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    lateinit var binding: HomeTabsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        binding = HomeTabsBinding.inflate(layoutInflater)
//        return binding.root
        val view: View = inflater.inflate(R.layout.home_tabs, container, false)
        viewPager = view.findViewById(R.id.viewpager)
        tableLayout = view.findViewById(R.id.tabs)
        adapter = childFragmentManager?.let { TabAdapter(it) }
        adapter!!.addFragment(ExerciseChoice(), "Choice")
        adapter!!.addFragment(ExercisePT(), "Personal Training")
        viewPager!!.adapter = adapter
        tableLayout!!.setupWithViewPager(viewPager)
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        adapter = childFragmentManager?.let { TabAdapter(it) }
//        adapter!!.addFragment(ExerciseChoice(), "Choice")
//        adapter!!.addFragment(ExercisePT(), "Personal Training")
//        binding.viewpager!!.adapter=adapter
//        binding.tabs!!.setupWithViewPager(viewPager)
//    }
}