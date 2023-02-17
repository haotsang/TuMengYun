package com.example.myapplication.utils.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnItemClickListener(onItemClickListener: ((holder: RecyclerView.ViewHolder, position: Int) -> Unit)?) {
    val mAttachListener: RecyclerView.OnChildAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (onItemClickListener != null) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(it)
                    onItemClickListener.invoke(holder, holder.adapterPosition)
                }
            }
        }
        override fun onChildViewDetachedFromWindow(view: View) {}
    }

    addOnChildAttachStateChangeListener(mAttachListener)
//    removeOnChildAttachStateChangeListener(mAttachListener)
}

fun RecyclerView.setOnItemLongClickListener(onItemLongClickListener: ((holder: RecyclerView.ViewHolder, position: Int) -> Boolean)?) {
    val mAttachListener: RecyclerView.OnChildAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            if (onItemLongClickListener != null) {
                view.setOnLongClickListener {
                    val holder = getChildViewHolder(it)
                    onItemLongClickListener.invoke(holder, holder.adapterPosition)
                }
            }
        }
        override fun onChildViewDetachedFromWindow(view: View) {}
    }

    addOnChildAttachStateChangeListener(mAttachListener)
//    removeOnChildAttachStateChangeListener(mAttachListener)
}
