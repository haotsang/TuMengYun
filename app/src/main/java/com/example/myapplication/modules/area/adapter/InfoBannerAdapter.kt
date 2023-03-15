package com.example.myapplication.modules.area.adapter

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
import com.example.myapplication.modules.area.entity.ExhibitDesc
import com.youth.banner.adapter.BannerAdapter

class InfoBannerAdapter(imageUrls: MutableList<ExhibitDesc>, val context: Context) :
    BannerAdapter<ExhibitDesc, InfoBannerAdapter.ImageHolder>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return ImageHolder(view)
    }

    override fun onBindView(holder: ImageHolder, data: ExhibitDesc, position: Int, size: Int) {
        Glide.with(holder.itemView)
            .load(data.url)
            .placeholder(R.drawable.bg)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageView)

        holder.tvTitle.text = data.content
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        //        var imageView: ImageView = view as ImageView
        var imageView = view.findViewById(R.id.img_banner) as ImageView
        var tvTitle = view.findViewById(R.id.tv_title) as TextView
    }

}
