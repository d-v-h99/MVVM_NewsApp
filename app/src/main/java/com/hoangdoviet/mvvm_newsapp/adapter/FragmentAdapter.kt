package com.hoangdoviet.mvvm_newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hoangdoviet.mvvm_newsapp.ui.fragment.BusinessFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.EntertainmentFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.GeneralFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.HealthFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.ScienceFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.SportsFragment
import com.hoangdoviet.mvvm_newsapp.ui.fragment.TechFragment
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.TOTAL_NEWS_TAB

class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) :  FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = TOTAL_NEWS_TAB

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return GeneralFragment()
            }
            1 -> {
                return BusinessFragment()
            }
            2 -> {
                return EntertainmentFragment()
            }
            3 -> {
                return ScienceFragment()
            }
            4 -> {
                return SportsFragment()
            }
            5 -> {
                return TechFragment()
            }
            6 -> {
                return HealthFragment()
            }

            else -> return BusinessFragment()

        }
    }
}