package com.example.myapplication.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.databinding.ViewDialogBinding
import com.google.android.material.internal.ViewUtils


class CustomDialog(private val builder: Builder2) : AlertDialog(builder.context) {

    private lateinit var binding: ViewDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val window: Window? = window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)

        initView(builder)
        //确定和取消按钮的事件监听
        initEvent(builder)
        //设置参数必须在show之后，不然没有效果
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.horizontalMargin = 0f
        params?.verticalMargin = 0f
        window?.attributes = params
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun initView(builder: Builder2) {
        builder.icon?.let { binding.dialogIcon.setImageResource(it) }

        if (builder.customView == null) {
            val tv = View.inflate(context, R.layout.view_dialog_title, null)
            tv.findViewById<TextView>(R.id.dialog_title).text = builder.title
            binding.dialogContent.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            binding.dialogContent.addView(builder.customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        binding.dialogCancelButton.text = builder.cancelText
        binding.dialogConfirmButton.text = builder.confirmText

        if (builder.cancelListener == null && builder.confirmListener == null) {
            binding.dialogButtonLayout.visibility = View.GONE
            binding.dialogHDiver.visibility = View.GONE
        } else {
            if (builder.cancelListener == null) {
                binding.dialogCancelButton.visibility = View.GONE
                binding.dialogVDiver.visibility = View.GONE
            }

            if (builder.confirmListener == null) {
                binding.dialogConfirmButton.visibility = View.GONE
                binding.dialogVDiver.visibility = View.GONE
            }
        }

        setCancelable(builder.cancelable)
    }

    private fun initEvent(builder: Builder2) {
        binding.dialogCancelButton.setOnClickListener {
            builder.cancelListener?.invoke()
            dismiss()
        }
        binding.dialogConfirmButton.setOnClickListener {
            builder.confirmListener?.invoke()
            dismiss()
        }

    }

    class Builder2(val context: Context) {
        var icon: Int? = null
        var title: CharSequence? = null
        var confirmText: CharSequence? = "确定"
        var cancelText: CharSequence? = "取消"

        var cancelListener: (() -> Unit)? = null
        var confirmListener: (() -> Unit)? = null

        var customView: View? = null

        var cancelable: Boolean = true

        fun setIcon(icon: Int?): Builder2 {
            this.icon = icon
            return this
        }

        fun setTitle(title: CharSequence): Builder2 {
            this.title = title
            return this
        }
        fun setTitle(id: Int): Builder2 {
            this.title = context.getString(id)
            return this
        }

        fun setConfirmText(t: CharSequence): Builder2 {
            this.confirmText = t
            return this
        }
        fun setConfirmText(id: Int): Builder2 {
            this.confirmText = context.getString(id)
            return this
        }
        fun setCancelText(t: CharSequence): Builder2 {
            this.cancelText = t
            return this
        }
        fun setCancelText(id: Int): Builder2 {
            this.cancelText = context.getString(id)
            return this
        }

        fun setCustomView(v: View?): Builder2 {
            this.customView = v
            return this
        }

        fun setCancelable(cancel: Boolean): Builder2 {
            this.cancelable = cancel
            return this
        }

        /**
         * 设置取消按钮的监听
         * @param cancelListener
         */
        fun setCancelListener(cancelListener: (() -> Unit)?): Builder2 {
            this.cancelListener = cancelListener
            return this
        }
        /**
         * 设置确定按钮的监听
         * @param confirmListener
         */
        fun setConfirmListener(confirmListener: (() -> Unit)?): Builder2 {
            this.confirmListener = confirmListener
            return this
        }

        /**
         * 指挥者，生成具体的提示框
         **/
        fun build(): CustomDialog {
            return CustomDialog(this)
        }

        /**
         * 指挥者，生成具体的提示框
         **/
        fun show(): CustomDialog {
            val dialog = build()
            dialog.show()
            return dialog
        }

    }

}