package com.aptiv.watchdogapp.util

import android.util.Base64
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.toggleVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}


fun ImageView.loadFromBase64(base64: String) {
    val imageByteArray = Base64.decode(base64, Base64.DEFAULT)

    Glide.with(this.context)
        .asBitmap()
        .load(imageByteArray)
        .into(this)
}
