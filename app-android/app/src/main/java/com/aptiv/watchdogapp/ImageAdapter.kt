package com.aptiv.watchdogapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.data.image.CapturedImage
import android.widget.ImageView
import com.aptiv.watchdogapp.util.DateHelper
import com.aptiv.watchdogapp.util.loadFromBase64
import java.util.*

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
        if (photos.firstOrNull()?.timestamp == newItems.firstOrNull()?.timestamp) {
            return
        }

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

    class PhotoHolder(view: View, private val clickListener: ((item: CapturedImage) -> Unit)) : RecyclerView.ViewHolder(view) {
        private val timestamp: TextView = view.findViewById(R.id.itemDate)
        private val image: ImageView = view.findViewById(R.id.itemImage)

        fun bindPhoto(photo: CapturedImage) {
            image.loadFromBase64(photo.value)
            timestamp.text = DateHelper.formatTimestamp(photo.timestamp)

            image.setOnLongClickListener {
                clickListener.invoke(photo)
                true
            }
        }
    }
}

