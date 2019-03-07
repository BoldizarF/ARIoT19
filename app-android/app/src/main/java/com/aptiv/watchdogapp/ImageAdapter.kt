package com.aptiv.watchdogapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.data.image.CapturedImage
import com.bumptech.glide.Glide
import android.util.Base64
import android.widget.ImageView

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.PhotoHolder>()  {

    private var photos = ArrayList<CapturedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bindPhoto(photos[position])
    }

    override fun getItemCount() = photos.size

    fun addImages(newItems: List<CapturedImage>) {
        photos.addAll(newItems)
        notifyDataSetChanged()
    }

    class PhotoHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val temp: TextView = view.findViewById(R.id.itemDate)
        private val image: ImageView = view.findViewById(R.id.itemImage)

        fun bindPhoto(photo: CapturedImage) {
            val imageByteArray = Base64.decode(photo.value, Base64.DEFAULT)

            Glide.with(view.context)
                .asBitmap()
                .load(imageByteArray)
                .into(image)


            temp.text = photo.timestamp.toString()
        }
    }
}

