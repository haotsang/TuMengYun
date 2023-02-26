package com.example.myapplication.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.myapplication.R


class MultiStatusButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

    init {
        setStatus(true)
    }

    fun setStatus(flag: Boolean) {
        if (flag) {
            setBackgroundResource(R.drawable.selector_login_btn_enabled)
            setTextColor(ContextCompat.getColorStateList(context, R.color.text_selector_enabled))
        } else {
            setBackgroundResource(R.drawable.selector_login_btn_disabled)
            setTextColor(ContextCompat.getColorStateList(context, R.color.text_selector_disabled))
        }
    }
}