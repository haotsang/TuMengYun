package com.example.myapplication

import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class MyPagerAdapter(private val list: MutableList<String>) : PagerAdapter() {

    var onView1ClickListener: ((view: View) -> Unit)? = null
    var onView2ClickListener: ((view: View) -> Unit)? = null


    override fun getCount(): Int = list.size
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = View.inflate(container.context, R.layout.item_photo, null)

        val photoView = v.findViewById<ImageView>(R.id.photo_thumbnail)
        photoView.setOnClickListener {
            onView1ClickListener?.invoke(it)
        }
        photoView.setImageBitmap(BitmapFactory.decodeFile(list[position]))

        val deleteBtn = v.findViewById<ImageView>(R.id.photo_delete)
        deleteBtn.setOnClickListener {
            onView2ClickListener?.invoke(it)
        }

        container.addView(v)
        return v
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}