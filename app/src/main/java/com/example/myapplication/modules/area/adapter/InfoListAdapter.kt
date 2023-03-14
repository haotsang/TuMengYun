package com.example.myapplication.modules.area.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemLocationInfoBinding
import com.example.myapplication.entity.BannerSimpleItem
import com.example.myapplication.entity.LocationInfo

class InfoListAdapter(private val context: Context) :
    RecyclerView.Adapter<InfoListAdapter.ItemViewHolder>() {

    val list = mutableListOf<LocationInfo>()

    init {
        val img = arrayListOf(
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18yG9z.img&ehk=aYiRRmfz8lGX3AVlaqAHbW%2bsODZDrrcfpgVlkx3j%2b%2bE%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18zxZ5.img&ehk=szGkOuY31f6BRUHNGVyh05UhvRToS8as8MAVSdTWNUs%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18ywD7.img&ehk=7BMj0fRwAQ4jya8sRsF8tfVvZJUIKj5YY1qHuHxZsVQ%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0"
        )
        val img2 = arrayListOf(
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18zHy9.img&ehk=1WlqV3b3CvNhbzQ2t0IOiDcCZlByESe39NpINlhJY%2b4%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18yr4c.img&ehk=v4b9%2bPSvEmOk%2fBuQMqMrMmyoaR8DDvA34fUduqNTr3k%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAAW7tjB.img&ehk=yLsU%2fBkaAbg0lt5OLbTJX60aLbcUjkJDmeu%2fDfAWguI%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0"
        )

        val img3 = arrayListOf(
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA18nJva.img&ehk=pyI%2fbA4Hugdo2odSGExG7DTOsPS6RzthZ0xE1hEnCnA%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAAZOzld.img&ehk=nP76oyJ5jlpgOPgHs7188wpSjSKQv7d4%2bPGvrYxCsCY%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0",
            "https://th.bing.com/th?u=https%3a%2f%2fimg-s.msn.cn%2ftenant%2famp%2fentityid%2fAA13lbAu.img&ehk=L8dcoo6OpzilK3QuMxEXPd%2fEK%2bJnufd2hK488VT4YO0%3d&w=186&h=88&c=8&rs=2&o=6&pid=WP0"
        )

        list.add(LocationInfo().apply {
            name = "展品1"
            bannerList = arrayListOf(BannerSimpleItem("banner1", img.get(0)), BannerSimpleItem("banner2", img.get(1)), BannerSimpleItem("banner3", img.get(2)))
        })
        list.add(LocationInfo().apply {
            name = "展品2"
            bannerList = arrayListOf(BannerSimpleItem("banner21", img2.get(0)), BannerSimpleItem("banner22", img2.get(1)), BannerSimpleItem("banner23", img2.get(2)))
        })
        list.add(LocationInfo().apply {
            name = "展品3"
            bannerList = arrayListOf(BannerSimpleItem("banner31", img.get(1)), BannerSimpleItem("banner32", img2.get(2)))
        })
        list.add(LocationInfo().apply {
            name = "展品4"
            bannerList = arrayListOf(BannerSimpleItem("banner41", img3.get(0)), BannerSimpleItem("banner42", img3.get(1)), BannerSimpleItem("banner43", img3.get(2)))
        })
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