package com.media.schoolday.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.media.schoolday.fragment.HomeFragment
import java.lang.ref.WeakReference

/*
 * Created by yosi on 03/05/2017.
 */

class TabPageAdapter(title: Array<String>,fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mTitle = title

    private val instanceFragment = SparseArray<WeakReference<Fragment>>()

    override fun getItem(position: Int): Fragment? {
        return when(position) {
            0 -> HomeFragment.newInstance(mTitle[position])
            1 -> HomeFragment.newInstance(mTitle[position])
            else -> return null
        }
    }

    override fun getCount(): Int = mTitle.size

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        instanceFragment.put(position, WeakReference<Fragment>(fragment))
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        instanceFragment.remove(position    )
        super.destroyItem(container, position, `object`)
    }

    fun getFragment(position : Int): Fragment? = instanceFragment.get(position).get()

    override fun getPageTitle(position: Int): CharSequence = mTitle[position]

//    fun addPager(title: String){
//        mTitle
//    }

}