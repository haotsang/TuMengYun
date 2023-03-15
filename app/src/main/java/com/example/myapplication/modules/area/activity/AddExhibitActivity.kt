package com.example.myapplication.modules.area.activity


import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.forEach
import com.example.myapplication.R
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.databinding.ActivityAddExhibitBinding
import com.example.myapplication.view.MultiStatusButton

class AddExhibitActivity : BaseActivity() {

    private lateinit var binding: ActivityAddExhibitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExhibitBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarBack.setOnClickListener { finish() }
        binding.baseOverflow.setOnClickListener {
            if (binding.linearContainer.childCount > 0) {
                finish()
            } else {
                Toast.makeText(this, "你还没有添加详情", Toast.LENGTH_SHORT).show()
            }
        }


        binding.continueAdd.setOnClickListener {
            if (binding.linearContainer.childCount > 5) {
                Toast.makeText(this, "添加太多了", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.linearContainer.addView(newChild())
            binding.scrollController.post { binding.scrollController.fullScroll(View.FOCUS_DOWN) }
        }

        binding.linearContainer.addView(newChild())

    }

    private fun newChild(): View {
        val v = View.inflate(this, R.layout.view_exhibit_item, null)

        val tips = v.findViewById<TextView>(R.id.exhibit_tips)
        val edit = v.findViewById<EditText>(R.id.exhibit_edit)
        val img = v.findViewById<ImageView>(R.id.exhibit_img)
        val delete = v.findViewById<ImageView>(R.id.exhibit_delete)

        val count = binding.linearContainer.childCount + 1
        tips.text = "详情$count："

        img.setOnClickListener {

        }
        delete.setOnClickListener {
            if (binding.linearContainer.childCount > 1) {
                binding.linearContainer.removeView(v)
                refreshChild()
            } else {
                Toast.makeText(this, "请至少保留一个", Toast.LENGTH_SHORT).show()
            }
        }
        return v
    }

    private fun refreshChild() {
        for ((index, child) in binding.linearContainer.children.withIndex()) {
            if (child is LinearLayout) {
                for (son in child.children) {
                    if (son is TextView) {
                        son.text = "详情${index + 1}："
                    }
                }
            }
        }

    }
}