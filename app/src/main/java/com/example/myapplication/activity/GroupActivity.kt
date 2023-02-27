package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBaseListBinding
import com.example.myapplication.entity.UserBean
import com.example.myapplication.http.UserUtils
import com.example.myapplication.utils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseListBinding

    private val list = mutableListOf<UserBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.setBarsFontLightColor(this, true)
        binding = ActivityBaseListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.baseTitle.text = "突梦群（0）"
        binding.baseBack.setOnClickListener { finish() }

        binding.baseRecyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_nav, parent, false)
                return object : RecyclerView.ViewHolder(view){}
            }
            override fun getItemCount(): Int = list.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = list[holder.adapterPosition]
                val icon = holder.itemView.findViewById<ImageView>(R.id.item_nav_icon)
                val title = holder.itemView.findViewById<TextView>(R.id.item_nav_title)
                val arrow = holder.itemView.findViewById<ImageView>(R.id.item_nav_arrow)

                icon.setImageResource(R.drawable.ic_nav_group)
                title.text = "${item.account}@${item.nickname}"
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = try {
                UserUtils.getAllUsers()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
            withContext(Dispatchers.Main) {
                list.clear()
                if (subList != null) {
                    list.addAll(subList)
                }
                binding.baseRecyclerView.adapter?.notifyDataSetChanged()

                binding.baseTitle.text = "突梦群（${list.size}）"
            }
        }

    }
}