package com.example.capstone_navi_tab.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragmentList: MutableList<Fragment> = ArrayList<Fragment>()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }
}