package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.entity.BannerItem
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by yechaoa on 2021/2/5.
 * Describe :
 */
class BannerImageAdapter2(imageUrls: MutableList<BannerItem>, val context: Context) :
    BannerAdapter<BannerItem, BannerImageAdapter2.ImageHolder>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return ImageHolder(view)
    }

    override fun onBindView(holder: ImageHolder, data: BannerItem, position: Int, size: Int) {
        Glide.with(holder.itemView)
            .load(data.imagePath)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageView)

        holder.tvTitle.text = data.title
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        var imageView: ImageView = view as ImageView
        var imageView = view.findViewById(R.id.img_banner) as ImageView
        var tvTitle = view.findViewById(R.id.tv_title) as TextView
    }

}
