package com.aptiv.watchdogapp.recyclerview

import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Photo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aptiv.watchdogapp.R

class RecyclerAdapter(private val photos: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        return PhotoHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var photo: Photo? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }

        fun bindPhoto(photo: Photo) {
            this.photo = photo
        }
    }
}
