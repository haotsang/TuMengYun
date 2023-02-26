package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.CangKuItem
import com.example.myapplication.http.HttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CangkuActivity : AppCompatActivity() {

    private val list = mutableListOf<CangKuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                tv.setText("ID:"+ item.id + "    名称：" +item.name +"    代号："+item.daihao +"    作用："+item.zuoyong +"    作标："+item.zuobiao)

            }
        }



        lifecycleScope.launch(Dispatchers.IO) {
            val json = HttpUtils.getLingJianAll()
            if (json != null) {
                val subList = HttpUtils.jsonToList(json)

                withContext(Dispatchers.Main) {
                    list.clear()
                    list.addAll(subList)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

        }

        setContentView(recyclerView)
    }

}