package com.example.myapplication.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class BaseRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
        layoutManager = LinearLayoutManager(context)
        val itemDecoration = CustomDecoration(
            context, (layoutManager as LinearLayoutManager).orientation, false
        )
        ContextCompat.getDrawable(context, R.color.ripper)?.let { itemDecoration.setDrawable(it) }
        addItemDecoration(itemDecoration)
    }
}
