package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.AdminBean
import com.example.myapplication.databinding.ActivityGroupBinding
import com.example.myapplication.utils.AdminUtils
import com.example.myapplication.utils.Prefs
import com.example.myapplication.utils.extensions.setOnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupBinding

    private val list = mutableListOf<AdminBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.title = "主题设置"
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cangku, parent, false)
                return object : RecyclerView.ViewHolder(view){}
            }
            override fun getItemCount(): Int = list.size
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = list[holder.adapterPosition]
                val tv = holder.itemView.findViewById<TextView>(R.id.item_name)
                tv.setText(item.name + "@" + item.contactName)
            }
        }
        binding.recyclerView.setOnItemClickListener { holder, position ->
            val obj = JSONObject()
            obj.put("id", list[position].id)
            obj.put("name", list[position].name)
            Prefs.curRegionId = obj.toString()

            Toast.makeText(this, "切换成功", Toast.LENGTH_SHORT).show()
            finish()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val subList = AdminUtils.getAll()

            withContext(Dispatchers.Main) {
                list.clear()
                list.addAll(subList)
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
        }

    }
}