package com.example.myapplication.utils.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.toColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}