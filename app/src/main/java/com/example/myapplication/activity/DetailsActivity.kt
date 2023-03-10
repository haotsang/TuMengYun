package com.example.myapplication.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import uk.co.senab.photoview.PhotoView

class DetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoView = PhotoView(this)
        photoView.setOnClickListener { finish() }
        Glide.with(photoView.context).load(intent.getStringExtra("path")).into(photoView)
//        photoView.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra("path")))

        setContentView(photoView)
    }
}