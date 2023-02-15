package com.example.myapplication.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import uk.co.senab.photoview.PhotoView

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoView = PhotoView(this)
        photoView.setOnClickListener { finish() }
        Glide.with(photoView.context).load(intent.getStringExtra("path")).into(photoView)
//        photoView.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra("path")))

        setContentView(photoView)
    }
}