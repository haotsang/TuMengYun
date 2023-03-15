package com.example.myapplication.modules.area.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemLocationInfoBinding
import com.example.myapplication.modules.area.entity.ExhibitBean

class InfoListAdapter(private val context: Context) :
    RecyclerView.Adapter<InfoListAdapter.ItemViewHolder>() {

    val list = mutableListOf<ExhibitBean>()

    fun setDataList(l2: List<ExhibitBean>) {
        list.clear()
        list.addAll(l2)
        notifyDataSetChanged()
    }


    var selection = 0
        set(choice) {
            field = choice
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemLocationInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setItem(holder)
    }

    inner class ItemViewHolder(private val binding: ItemLocationInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(holder: ItemViewHolder) {
            val single = list[holder.absoluteAdapterPosition]

            binding.itemInfoIcon.visibility = View.GONE
            binding.itemInfoTitle.text = single.name
            binding.itemInfoArrow.setImageResource(if (selection == holder.absoluteAdapterPosition) R.drawable.baseline_check_24 else 0)
        }
    }
}