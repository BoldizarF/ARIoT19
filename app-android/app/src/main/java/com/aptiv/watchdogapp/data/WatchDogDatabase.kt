package com.aptiv.watchdogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aptiv.watchdogapp.data.health.local.HealthDao
import com.aptiv.watchdogapp.data.health.local.HeartRateEntity
import com.aptiv.watchdogapp.data.image.local.ImageDao
import com.aptiv.watchdogapp.data.image.local.ImageEntity

@Database(entities = [
    ImageEntity::class,
    HeartRateEntity::class
], version = 1)
abstract class WatchDogDatabase : RoomDatabase() {

    abstract fun healthDao(): HealthDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: WatchDogDatabase? = null

        fun getDatabase(context: Context): WatchDogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchDogDatabase::class.java,
                    "WatchDog.db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
