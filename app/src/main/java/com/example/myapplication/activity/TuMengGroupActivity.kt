package com.example.myapplication.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.utils.LoginUtils

class TuMengGroupActivity : AppCompatActivity() {

    private val list = mutableListOf<String>()


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
                tv.setText(item)

            }
        }



        Thread{
            val json = LoginUtils.getAllUsers()
            runOnUiThread {
                list.clear()
                list.addAll(json)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }.start()



        setContentView(recyclerView)


    }
}