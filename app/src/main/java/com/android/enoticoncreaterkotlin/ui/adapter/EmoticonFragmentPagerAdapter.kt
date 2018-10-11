package com.android.enoticoncreaterkotlin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.enoticoncreaterkotlin.ui.fragment.EmoticonFragment
import java.util.*

class EmoticonFragmentPagerAdapter(fm: FragmentManager, titles: Array<String>) : FragmentPagerAdapter(fm) {

    private var mFragments: ArrayList<Fragment> = ArrayList()
    private var mTitles: Array<String>? = titles

    init {
        this.mTitles = titles
        mFragments = ArrayList()
        for (title in mTitles!!) {
            mFragments.add(EmoticonFragment.newInstance(title))
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles!![position]
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }
}