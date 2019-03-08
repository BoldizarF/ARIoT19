package com.aptiv.watchdogapp.util

import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromBase64(base64: String) {
    val imageByteArray = Base64.decode(base64, Base64.DEFAULT)

    Glide.with(this.context)
        .asBitmap()
        .load(imageByteArray)
        .into(this)
}
