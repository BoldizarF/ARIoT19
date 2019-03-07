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

class ImageAdapter(private val clickListener: ((item: CapturedImage) -> Unit)) : RecyclerView.Adapter<ImageAdapter.PhotoHolder>()  {

    private var photos = ArrayList<CapturedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row_image,
            parent,
            false
        )

        return PhotoHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bindPhoto(photos[position])
    }

    override fun getItemCount() = photos.size

    fun addImages(newItems: List<CapturedImage>) {
        photos.clear()
        photos.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeImage(imageId: Long) {
        photos.removeIf {
            it.timestamp == imageId
        }
        notifyDataSetChanged()
    }

    class PhotoHolder(private val view: View, private val clickListener: ((item: CapturedImage) -> Unit)) : RecyclerView.ViewHolder(view) {
        private val timestamp: TextView = view.findViewById(R.id.itemDate)
        private val image: ImageView = view.findViewById(R.id.itemImage)

        fun bindPhoto(photo: CapturedImage) {
            image.loadFromBase64(photo.value)
            timestamp.text = formatTimestamp(photo.timestamp)

            image.setOnLongClickListener {
                clickListener.invoke(photo)
                true
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            // convert seconds to milliseconds
            val date = java.util.Date(timestamp * 1000L)
            // the format of your date
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
            // give a timezone reference for formatting (see comment at the bottom)
            sdf.timeZone = java.util.TimeZone.getTimeZone("GMT+1")
            return sdf.format(date)
        }
    }
}

