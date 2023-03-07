package com.example.myapplication.entity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class BannerItem(
    var id: Int?=null,
    var imagePath: String?=null,
    var lid: Int?=null,
    var bitmap: Drawable? = null
)