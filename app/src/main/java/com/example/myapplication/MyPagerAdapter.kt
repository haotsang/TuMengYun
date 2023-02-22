package com.example.myapplication

import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class MyPagerAdapter(private val list: MutableList<View>) : PagerAdapter() {


    override fun getCount(): Int = list.size
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = list[position]
        container.addView(v)
        return v
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(list[position])
    }
}