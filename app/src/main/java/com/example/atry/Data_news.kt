package com.example.atry

import android.graphics.Bitmap

data class Data_news(
    val newsId: Int,
    val hline: String,
    val intro: String,
    val imageId: String,
    var image: Bitmap? = null
)

