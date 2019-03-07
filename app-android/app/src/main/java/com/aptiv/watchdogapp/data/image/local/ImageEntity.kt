package com.aptiv.watchdogapp.data.image.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_items")
class ImageEntity(
    val value: String,
    val timestamp: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

