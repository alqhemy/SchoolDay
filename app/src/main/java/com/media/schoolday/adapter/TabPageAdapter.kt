package com.media.schoolday.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.media.schoolday.fragment.HomeFragment
import com.media.schoolday.fragment.ProfileFragment

/*
 * Created by yosi on 03/05/2017.
 */

class TabPageAdapter(title: Array<String>,fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mTitle = title
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment.newInstance(mTitle[position])
            else -> ProfileFragment.getInstance(mTitle[position])
        }

    }

    override fun getCount(): Int {
        return mTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitle[position]
    }
    fun addPager(title: String){
        mTitle
    }
}