package com.example.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.utils.extensions.toColor

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

    var emptyText: String? = context.getString(R.string.empty)
    var show = false
    private var isEmpty: Boolean = true


    //数据监听者
    private val adapterDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    private fun checkIfEmpty() {
        if (adapter != null) {
            val emptyViewVisible = adapter?.itemCount == 0
            isEmpty = emptyViewVisible
        }
        invalidate()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(adapterDataObserver)
        checkIfEmpty()
    }


    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (show) {
            canvas?.drawColor(Color.CYAN)
        } else {
            canvas?.drawColor(Color.TRANSPARENT)

            if (isEmpty) {
                val paint = Paint().apply {
                    color = context.toColor(R.color.hint)
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                    textSize = TypedValue.applyDimension(1, 16.0f, resources.displayMetrics).toInt().toFloat()
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                }


                val xPos = width / 2F
                val yPos = height / 2 - (paint.descent() + paint.ascent()) / 2

                emptyText?.let { canvas?.drawText(it, xPos, yPos, paint) }
            }
        }
    }

}
