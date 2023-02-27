package com.example.myapplication.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R


class CustomDialog(context: Context) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_dialog)
        val window: Window? = window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)

        //初始化布局控件
        initView()
        //确定和取消按钮的事件监听
        initEvent()
        //设置参数必须在show之后，不然没有效果
        val params = window?.attributes
        window?.attributes = params

    }

    private fun initEvent() {

    }

    private fun initView() {

    }
}